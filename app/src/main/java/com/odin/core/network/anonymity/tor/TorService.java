package com.odin.core.network.anonymity.tor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.odin.core.R;

/**
 * Foreground service for managing Tor connection.
 * Enterprise-grade Tor service for ODIN.
 */
public class TorService extends Service {
    private static final String TAG = "TorService";
    private static final String CHANNEL_ID = "TorServiceChannel";
    private static final int NOTIFICATION_ID = 1001;

    private final IBinder binder = new LocalBinder();
    private TorConnectionManager torConnectionManager;
    private TorCircuitManager torCircuitManager;
    private TorBridgeManager torBridgeManager;
    private TorTransportManager torTransportManager;
    private TorMonitor torMonitor;
    private TorLogger torLogger;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "TorService created");

        // Initialize managers
        torConnectionManager = new TorConnectionManager(this);
        torCircuitManager = new TorCircuitManager();
        torBridgeManager = new TorBridgeManager();
        torTransportManager = new TorTransportManager();
        torLogger = new TorLogger();
        torMonitor = new TorMonitor(torConnectionManager, torCircuitManager);

        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "TorService started");
        
        // Start as foreground service
        startForeground(NOTIFICATION_ID, createNotification());
        
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "TorService destroyed");

        // Stop Tor connection
        if (torConnectionManager != null) {
            torConnectionManager.stopTor();
        }

        // Stop monitoring
        if (torMonitor != null) {
            torMonitor.shutdown();
        }

        // Shutdown connection manager
        if (torConnectionManager != null) {
            torConnectionManager.shutdown();
        }
    }

    /**
     * Start Tor connection
     */
    public void startTor() {
        torLogger.info("TorService", "Starting Tor connection");
        torConnectionManager.startTor();
        torMonitor.startMonitoring();
    }

    /**
     * Stop Tor connection
     */
    public void stopTor() {
        torLogger.info("TorService", "Stopping Tor connection");
        torConnectionManager.stopTor();
        torMonitor.stopMonitoring();
    }

    /**
     * Get Tor status
     */
    public com.odin.core.network.anonymity.model.TorStatus getTorStatus() {
        return torConnectionManager.getTorStatus();
    }

    /**
     * Get Tor logger
     */
    public TorLogger getTorLogger() {
        return torLogger;
    }

    /**
     * Get Tor circuit manager
     */
    public TorCircuitManager getTorCircuitManager() {
        return torCircuitManager;
    }

    /**
     * Get Tor bridge manager
     */
    public TorBridgeManager getTorBridgeManager() {
        return torBridgeManager;
    }

    /**
     * Get Tor transport manager
     */
    public TorTransportManager getTorTransportManager() {
        return torTransportManager;
    }

    /**
     * Create notification channel
     */
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Tor Service",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Tor anonymity service is running");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * Create notification
     */
    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ODIN Tor Service")
                .setContentText("Tor anonymity is active")
                .setSmallIcon(R.drawable.ic_odin_notification)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    /**
     * Local binder for client access
     */
    public class LocalBinder extends Binder {
        public TorService getService() {
            return TorService.this;
        }
    }
}
