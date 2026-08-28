package com.odin.core.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Build;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.odin.core.R;
import com.odin.core.netshield.NetShieldRuntimeState;
import com.odin.core.persistence.SentryCore;
import com.odin.core.di.DependencyProvider;
import com.odin.core.domain.model.NetworkPacket;
import com.odin.core.domain.model.LogEvent;
import com.odin.core.network.detection.DetectionResult;
import com.odin.core.network.detection.ThreatDetectionEngine;
import com.odin.core.network.detection.ThreatLevel;
import com.odin.core.network.dpi.DeepPacketInspector;
import com.odin.core.network.rules.DynamicRuleEngine;
import com.odin.core.security.ConsentManager;
import com.odin.core.service.network.LocalFirewallEngine;
import com.odin.core.service.network.PacketAuditor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NetShieldVpnService intercepta el tráfico de red del dispositivo a través de un adaptador VPN local.
 * La VPN es un túnel interno que permite auditar y bloquear paquetes antes de enviarlos.
 */
public class NetShieldVpnService extends VpnService {

    public static final String ACTION_START_VPN = "com.odin.core.action.START_VPN";
    public static final String ACTION_STOP_VPN = "com.odin.core.action.STOP_VPN";

    private static final String TAG = "NetShieldVpnService";
    private static final String CHANNEL_ID = "odin_netshield_channel";
    private static final String CHANNEL_NAME = "ODIN NetShield";
    private static final int NOTIFICATION_ID = 8902;
    private static final boolean SAFE_VALIDATION_MODE = true;
    private static final long SAFE_HEARTBEAT_INTERVAL_MS = 15_000L;

    private ParcelFileDescriptor vpnInterface;
    private Thread vpnThread;
    private volatile boolean running;

    private PacketAuditor packetAuditor;
    private LocalFirewallEngine firewallEngine;
    
    // ===== NetShield Engines (Sprint 1) =====
    private ThreatDetectionEngine threatDetectionEngine;
    private DynamicRuleEngine dynamicRuleEngine;
    private DeepPacketInspector deepPacketInspector;
    private SentryCore sentryCore;
    
    private long packetsProcessed = 0;
    private long threatsDetected = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        
        packetAuditor = new PacketAuditor();
        firewallEngine = new LocalFirewallEngine(
                DependencyProvider.provideFirewallRepository(this)
        );
        
        // ===== Inicializar NetShield engines =====
        try {
            threatDetectionEngine = new ThreatDetectionEngine(this); // Pass context for SentryCore
            
            dynamicRuleEngine = new DynamicRuleEngine();
            deepPacketInspector = new DeepPacketInspector();
            
            // Obtener SentryCore para forensics
            sentryCore = SentryCore.getInstance(this);
            
            // Configurar listeners para amenazas detectadas
            threatDetectionEngine.addListener(new ThreatDetectionEngine.ThreatDetectionListener() {
                @Override
                public void onThreatDetected(DetectionResult detection) {
                    onNetShieldThreatDetected(detection);
                }

                @Override
                public void onAnomalyDetected(String anomalyType, int severity) {
                    // Log anomalía
                    Log.w(TAG, "Anomalía detectada: " + anomalyType + " (severity=" + severity + ")");
                }
            });
            
            Log.i(TAG, "NetShield engines inicializados correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando NetShield engines", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_STOP_VPN.equals(intent.getAction())) {
            NetShieldRuntimeState.markStopping(this);
            stopVpn();
            return START_NOT_STICKY;
        }

        if (!canStartFromService()) {
            stopSelf();
            return START_NOT_STICKY;
        }

        startForeground(NOTIFICATION_ID, buildForegroundNotification());
        if (!startVpn()) {
            stopForeground(true);
            stopSelf();
            return START_NOT_STICKY;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        stopVpn();
        
        // ===== Shutdown NetShield engines =====
        if (threatDetectionEngine != null) {
            threatDetectionEngine.stop();
        }
        if (dynamicRuleEngine != null) {
            dynamicRuleEngine.shutdown();
        }
        
        // Log estadísticas finales
        if (sentryCore != null) {
            LogEvent finalStats = new LogEvent(
                    System.currentTimeMillis(),
                    "NETSHIELD_SHUTDOWN",
                    String.format("NetShield detenido. Paquetes: %d, Amenazas: %d", 
                            packetsProcessed, threatsDetected)
            );
            try {
                sentryCore.appendEvent(finalStats);
            } catch (Exception e) {
                // Ignorar errores de logging
            }
        }
        
        Log.i(TAG, String.format("NetShield shutdown. Stats: packets=%d, threats=%d", 
                packetsProcessed, threatsDetected));
        
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    private boolean startVpn() {
        if (running) {
            NetShieldRuntimeState.markActive(this);
            return true;
        }
        try {
            vpnInterface = prepareVpnInterface();
        } catch (RuntimeException e) {
            Log.e(TAG, "Error estableciendo interfaz VPN.", e);
            NetShieldRuntimeState.markError(this, "Error estableciendo interfaz VPN.");
            return false;
        }
        if (vpnInterface == null) {
            Log.e(TAG, "No se pudo establecer la interfaz VPN.");
            NetShieldRuntimeState.markError(this, "No se pudo establecer la interfaz VPN.");
            return false;
        }
        running = true;
        if (!SAFE_VALIDATION_MODE && threatDetectionEngine != null) {
            threatDetectionEngine.start();
        }
        vpnThread = new Thread(
                SAFE_VALIDATION_MODE ? this::runSafeValidationLoop : this::runVpnLoop,
                "NetShieldVpnThread"
        );
        vpnThread.start();
        NetShieldRuntimeState.markActive(this);
        logNetShieldEvent("NETSHIELD_STARTED", "NetShield iniciado en modo de validacion segura.", ThreatLevel.INFO);
        return true;
    }

    private void stopVpn() {
        running = false;
        closeInterface();
        Thread thread = vpnThread;
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join(1_500L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            vpnThread = null;
        }
        stopForeground(true);
        NetShieldRuntimeState.markStopped(this, "NetShield detenido.");
        stopSelf();
    }

    private ParcelFileDescriptor prepareVpnInterface() {
        Builder builder = new Builder();
        builder.setSession("ODIN NetShield");
        builder.setMtu(1500);
        builder.addAddress("10.0.0.1", 32);
        // Fase 8: modo seguro. No se enruta todo el trafico ni se fuerzan DNS
        // hasta implementar forwarding real validado.
        builder.allowFamily(android.system.OsConstants.AF_INET);
        return builder.establish();
    }

    private void runSafeValidationLoop() {
        while (running && !Thread.currentThread().isInterrupted()) {
            NetShieldRuntimeState.markHeartbeat(this);
            try {
                Thread.sleep(SAFE_HEARTBEAT_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void runVpnLoop() {
        try (FileInputStream fileInputStream = new FileInputStream(vpnInterface.getFileDescriptor());
             FileOutputStream fileOutputStream = new FileOutputStream(vpnInterface.getFileDescriptor());
             FileChannel inputChannel = fileInputStream.getChannel();
             FileChannel outputChannel = fileOutputStream.getChannel()) {

            ByteBuffer packetBuffer = ByteBuffer.allocate(32767);
            while (running && inputChannel.isOpen()) {
                packetBuffer.clear();
                int length = inputChannel.read(packetBuffer);
                if (length <= 0) {
                    continue;
                }
                packetBuffer.flip();
                byte[] packetBytes = new byte[length];
                packetBuffer.get(packetBytes);

                // ===== NetShield: Análisis de amenazas =====
                NetworkPacket auditedPacket = packetAuditor.auditPacket(packetBytes, length);
                packetsProcessed++;
                NetShieldRuntimeState.markPacketObserved(this, length, true);
                
                // Enviar a ThreatDetectionEngine para análisis asíncrono
                if (threatDetectionEngine != null) {
                    threatDetectionEngine.submitPacket(auditedPacket);
                }
                
                // Verificar firewall tradicional
                if (firewallEngine.shouldBlockPacket(auditedPacket)) {
                    logNetShieldEvent("PACKET_BLOCKED_LEGACY", "Paquete bloqueado; metadata sensible no expuesta.", ThreatLevel.MEDIUM);
                    continue;
                }

                // ===== Evaluación de reglas dinámicas =====
                DynamicRuleEngine.RuleAction ruleAction = DynamicRuleEngine.RuleAction.ALLOW;
                if (dynamicRuleEngine != null) {
                    ruleAction = dynamicRuleEngine.evaluatePacket(
                            auditedPacket.getSourceIp(),
                            auditedPacket.getDestinationIp(),
                            auditedPacket.getSourcePort(),
                            auditedPacket.getDestinationPort(),
                            auditedPacket.getProtocol(),
                            "DEFAULT"
                    ) != null ? DynamicRuleEngine.RuleAction.BLOCK : DynamicRuleEngine.RuleAction.ALLOW;
                }

                // Procesar según acción de regla
                switch (ruleAction) {
                    case BLOCK:
                        logNetShieldEvent("PACKET_BLOCKED_RULE", "Paquete bloqueado por regla; metadata sensible no expuesta.", ThreatLevel.HIGH);
                        continue;
                    case QUARANTINE:
                        logNetShieldEvent("PACKET_QUARANTINED", "Accion de cuarentena registrada sin metadata sensible.", ThreatLevel.MEDIUM);
                        continue;
                    case ALERT:
                        logNetShieldEvent("PACKET_ALERT", "Alerta de paquete sin metadata sensible.", ThreatLevel.LOW);
                        break;
                    case ALLOW:
                    case RATE_LIMIT:
                    default:
                        // Permitir
                        break;
                }

                // Permitir paquete
                logNetShieldEvent("PACKET_ALLOWED", "Paquete permitido; metadata sensible no expuesta.", ThreatLevel.INFO);
                packetBuffer.rewind();
                while (packetBuffer.hasRemaining()) {
                    outputChannel.write(packetBuffer);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error en bucle VPN: ", e);
            if (sentryCore != null) {
                try {
                    sentryCore.appendEvent(new LogEvent(
                            System.currentTimeMillis(),
                            "NETSHIELD_VPN_ERROR",
                            "VPN loop error: " + e.getMessage()
                    ));
                } catch (Exception ex) {
                    // Ignorar errores de logging
                }
            }
        } finally {
            closeInterface();
        }
    }
    
    /**
     * Callback cuando ThreatDetectionEngine detecta una amenaza.
     */
    private void onNetShieldThreatDetected(DetectionResult detection) {
        threatsDetected++;
        
        // Evaluar acción recomendada
        DynamicRuleEngine.RuleAction action = dynamicRuleEngine != null 
                ? dynamicRuleEngine.evaluateDetection(detection)
                : (detection.getThreatLevel().isAtLeast(ThreatLevel.MEDIUM) 
                        ? DynamicRuleEngine.RuleAction.BLOCK 
                        : DynamicRuleEngine.RuleAction.ALERT);
        
        // Log forense en SentryCore (encriptado) con severidad y metadata
        if (sentryCore != null) {
            java.util.Map<String, String> metadata = new java.util.HashMap<>();
            metadata.put("threatType", detection.getThreatType());
            metadata.put("confidence", String.valueOf(detection.getConfidence()));
            metadata.put("action", action.name());
            metadata.put("detector", detection.getDetectorName());
            
            // Mapear ThreatLevel a LogEvent.Severity
            LogEvent.Severity severity = mapThreatLevelToSeverity(detection.getThreatLevel());
            
            LogEvent forensicEvent = LogEvent.builder()
                    .timestamp(System.currentTimeMillis())
                    .source("THREAT_DETECTION")
                    .message(String.format(
                            "Type=%s, Confidence=%.0f%%, Action=%s",
                            detection.getThreatType(),
                            detection.getConfidence() * 100,
                            action.name()
                    ))
                    .severity(severity)
                    .metadata(metadata)
                    .build();
            
            try {
                sentryCore.appendEvent(forensicEvent);
            } catch (Exception e) {
                // Ignorar errores de logging
            }
        }
        
        // Auto-generar regla si es threat alto
        if (detection.getThreatLevel().isAtLeast(ThreatLevel.HIGH) && dynamicRuleEngine != null) {
            dynamicRuleEngine.autoGenerateRule(detection);
            Log.w(TAG, "Regla auto-generada para: " + detection.getThreatType());
        }
        
        Log.w(TAG, "Amenaza detectada por NetShield: " + detection.getThreatType());
    }
    
    /**
     * Mapea ThreatLevel a LogEvent.Severity.
     */
    private LogEvent.Severity mapThreatLevelToSeverity(ThreatLevel threatLevel) {
        if (threatLevel == ThreatLevel.CRITICAL) {
            return LogEvent.Severity.CRITICAL;
        } else if (threatLevel == ThreatLevel.HIGH) {
            return LogEvent.Severity.ERROR;
        } else if (threatLevel == ThreatLevel.MEDIUM) {
            return LogEvent.Severity.WARNING;
        } else {
            return LogEvent.Severity.INFO;
        }
    }
    
    /**
     * Log de evento de NetShield con severidad y metadata.
     */
    private void logNetShieldEvent(String eventType, String details, ThreatLevel level) {
        if (sentryCore != null) {
            try {
                LogEvent.Severity severity = mapThreatLevelToSeverity(level);
                
                java.util.Map<String, String> metadata = new java.util.HashMap<>();
                metadata.put("threatLevel", level.name());
                metadata.put("packetsProcessed", String.valueOf(packetsProcessed));
                metadata.put("threatsDetected", String.valueOf(threatsDetected));
                
                LogEvent event = LogEvent.builder()
                        .timestamp(System.currentTimeMillis())
                        .source(eventType)
                        .message(eventType + ": metadata de red sensible no expuesta.")
                        .severity(severity)
                        .metadata(metadata)
                        .build();
                
                sentryCore.appendEvent(event);
            } catch (Exception e) {
                // Ignorar errores de logging
            }
        }
        
        // Fase 8: no se emiten broadcasts globales con metadata de paquetes.
    }
    
    /**
     * Formatea IP para logging.
     */
    private static String formatIP(String ip) {
        return ip;
    }

    private Notification buildForegroundNotification() {
        Intent activityIntent = new Intent();
        activityIntent.setClassName(this, "com.odin.core.ui.MainActivity");
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ODIN NetShield activo")
                .setContentText("VPN local visible en ejecucion.")
                .setSmallIcon(R.drawable.ic_odin_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Canal de notificaciones para el servicio VPN de auditoría de red.");
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private boolean canStartFromService() {
        if (!ConsentManager.getInstance(this).isNetShieldVpnEnabled()) {
            NetShieldRuntimeState.markStopped(this, "Inicio bloqueado: falta consentimiento NetShield.");
            logNetShieldEvent("NETSHIELD_START_BLOCKED", "Falta consentimiento NetShield.", ThreatLevel.INFO);
            return false;
        }
        NetShieldRuntimeState.Snapshot snapshot = NetShieldRuntimeState.read(this);
        if (!snapshot.getVpnAuthorized()) {
            NetShieldRuntimeState.markStopped(this, "Inicio bloqueado: falta autorizacion VPN.");
            logNetShieldEvent("NETSHIELD_START_BLOCKED", "Falta autorizacion VPN.", ThreatLevel.INFO);
            return false;
        }
        if (!snapshot.getConnectivityRiskAccepted()) {
            NetShieldRuntimeState.markStopped(this, "Inicio bloqueado: advertencia de conectividad pendiente.");
            logNetShieldEvent("NETSHIELD_START_BLOCKED", "Advertencia de conectividad pendiente.", ThreatLevel.INFO);
            return false;
        }
        if (!hasNotificationPermission()) {
            NetShieldRuntimeState.markError(this, "Inicio bloqueado: falta permiso de notificaciones.");
            logNetShieldEvent("NETSHIELD_START_BLOCKED", "Falta permiso de notificaciones.", ThreatLevel.INFO);
            return false;
        }
        return true;
    }

    private boolean hasNotificationPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void closeInterface() {
        if (vpnInterface != null) {
            try {
                vpnInterface.close();
            } catch (IOException e) {
                Log.w(TAG, "Error cerrando la interfaz VPN.", e);
            } finally {
                vpnInterface = null;
            }
        }
    }
}
