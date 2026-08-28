# Fase 7 - SentryCore / SentryService / Foreground Service UI

Fase: 7 oficial.

Estado: Cerrada al 100% con validacion real en dispositivo `116829e90703`.

Archivos revisados:
- `app/src/main/java/com/odin/core/persistence/SentryCore.java`
- `app/src/main/java/com/odin/core/service/SentryService.java`
- `app/src/main/java/com/odin/core/receiver/BootReceiver.java`
- `app/src/main/java/com/odin/core/security/ConsentManager.java`
- `app/src/main/java/com/odin/core/ui/screens/services/ServicesStatusScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/dashboard/DashboardScreen.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/DashboardViewModel.kt`
- `app/src/main/java/com/odin/core/ui/screens/tools/ToolsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/permissions/PermissionsScreen.kt`
- `app/src/main/java/com/odin/core/ui/MainActivity.kt`
- `app/src/main/AndroidManifest.xml` solo lectura inicial.

Archivos modificados:
- `app/src/main/java/com/odin/core/service/SentryService.java`
- `app/src/main/java/com/odin/core/receiver/BootReceiver.java`
- `app/src/main/java/com/odin/core/security/CryptographicManager.java`
- `app/src/main/java/com/odin/core/persistence/SentryCore.java`
- `app/src/main/java/com/odin/core/sentry/SentryController.kt`
- `app/src/main/java/com/odin/core/sentry/SentryRuntimeState.kt`
- `app/src/main/java/com/odin/core/ui/model/SentryUiState.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/SentryViewModel.kt`
- `app/src/main/java/com/odin/core/ui/screens/sentry/SentryControlScreen.kt`
- `app/src/main/java/com/odin/core/ui/navigation/Screen.kt`
- `app/src/main/java/com/odin/core/ui/MainActivity.kt`
- `app/src/main/java/com/odin/core/ui/screens/dashboard/DashboardScreen.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/DashboardViewModel.kt`
- `app/src/main/java/com/odin/core/ui/screens/tools/ToolsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/permissions/PermissionsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/services/ServicesStatusScreen.kt`

Estado inicial de SentryCore:
- Existia como nucleo local con persistencia cifrada y eventos.
- Durante runtime real aparecio un fallo de cifrado AES-GCM: Android Keystore rechazaba IV provisto por el llamador.
- Correccion aplicada: `CryptographicManager.encrypt()` ahora deja que Keystore genere el IV cuando la clave exige `setRandomizedEncryptionRequired(true)`.

Estado inicial de SentryService:
- Servicio existente con riesgo de confundir consentimiento con estado activo.
- No habia una fuente UI dedicada y clara para consentimiento, inicio, detencion y heartbeat.

Flujo de consentimiento implementado:
- Pantalla `Sentry local` con explicacion visible.
- Consentimiento principal separado de consentimiento de inicio tras reinicio.
- `consent_sentry_enabled` no se activa al abrir la app.
- `consent_boot_sentry_enabled` no se activa por defecto.
- Revocar consentimiento detiene/inutiliza el flujo y deja Sentry en `Requiere consentimiento`.

Permiso de notificaciones:
- Android 13+ se valida antes de iniciar Sentry.
- El permiso se solicita solo por accion explicita desde pantalla Sentry si el dispositivo lo requiere.
- En el dispositivo de prueba `POST_NOTIFICATION: allow` ya estaba permitido por AppOps/OS; no se observo dialogo automatico al abrir ODIN.

Inicio de Sentry:
- Sentry inicia solo por accion explicita del usuario.
- `SentryController.startSentry()` bloquea inicio sin consentimiento o sin permiso de notificaciones requerido.
- `SentryService` usa `startForeground()` y reporta estado runtime via `SentryRuntimeState`.
- `START_NOT_STICKY` evita reinicios ocultos no controlados.

Detencion de Sentry:
- Boton `Detener` desde pantalla Sentry.
- Usa accion `ACTION_STOP_SENTRY`.
- Tras detener, `dumpsys activity services com.odin.core` no muestra `SentryService`.
- La notificacion foreground desaparece.

Revocacion de consentimiento:
- Revocar consentimiento no deja servicio corriendo.
- Boot consent queda desactivado o inutilizado.
- UI vuelve a `Requiere consentimiento`.

BootReceiver:
- Exige doble consentimiento:
  - `consent_sentry_enabled = true`
  - `consent_boot_sentry_enabled = true`
- Valida permiso de notificaciones en Android 13+.
- Registra decisiones de no inicio sin datos sensibles.
- No inicia Sentry con instalacion limpia ni con consentimiento principal revocado.

Notificacion foreground:
- Titulo validado: ODIN Sentry activo.
- Texto validado: Proteccion local visible en ejecucion.
- Canal usado: `odin_sentry_channel`.
- Foreground id observado: `8901`.
- Captura generada: `fase7_sentry_notification.png`.

Estado en Dashboard:
- Instalacion limpia/revocacion: `Requiere atencion`, `Postura: No disponible`.
- Sentry aparece como `Requiere consentimiento` si no hay consentimiento.
- No muestra `Protegido`, `Sistema seguro`, porcentajes falsos ni amenazas falsas.
- NetShield sigue como requiere consentimiento/autorizacion, sin activar VPN.

Estado en Servicios:
- SentryService aparece como `Requiere consentimiento` tras revocar.
- NetShieldVpnService aparece como informativo y no se inicia desde navegacion.
- Foreground services indica control pendiente/informativo.

Logs/eventos registrados:
- Eventos no sensibles de Sentry: consentimiento, inicio solicitado, detencion, revocacion y bloqueos por permiso/consentimiento.
- No se registran datos privados del usuario.
- Se mejoro logging de errores reales en `SentryCore` y `SentryService` para no ocultar fallos criticos.

Validacion compileDebugKotlin:
- Comando: `.\gradlew.bat :app:compileDebugKotlin --console=plain --no-daemon`
- Resultado: `BUILD SUCCESSFUL`.

Validacion assembleDebug:
- Comando: `.\gradlew.bat :app:assembleDebug --console=plain --no-daemon`
- Resultado: `BUILD SUCCESSFUL`.

Resultado instalacion:
- `adb devices`: `116829e90703 device`.
- `pm uninstall com.odin.core`: `Success` en instalacion limpia.
- `adb push app\build\outputs\apk\debug\app-debug.apk /data/local/tmp/odin-app-debug.apk`: correcto.
- `pm install -r /data/local/tmp/odin-app-debug.apk`: `Success`.

Resultado apertura inicial:
- App abre con `adb shell monkey -p com.odin.core 1`.
- No se inicia Sentry al abrir.
- No se inicia NetShield/VPN al abrir.
- No se solicita permiso al abrir.

Resultado logcat inicial:
- Sin `FATAL EXCEPTION` de ODIN.
- Sin crash Hilt.
- Sin `Resources.NotFoundException`.
- Sin `InflateException`.
- Sin `ForegroundServiceStartNotAllowedException`.
- Sin `SecurityException` de ODIN.
- Ruido externo observado de MIUI/sistema, no asociado a crash de ODIN.

Resultado servicios inicial:
- `dumpsys activity services com.odin.core`: sin `SentryService`, sin `NetShieldVpnService`, sin foreground services activos.

Resultado inicio Sentry:
- Tras consentimiento y accion explicita, `SentryService` inicia correctamente.
- Log observado: `SentryService inicializado exitosamente`.
- SentryCore queda saludable tras corregir cifrado.

Resultado dumpsys services con Sentry activo:
- `ServiceRecord ... com.odin.core/.service.SentryService`
- `isForeground=true`
- `foregroundId=8901`
- `channelId=odin_sentry_channel`
- No aparece `NetShieldVpnService`.

Resultado notificacion:
- Notificacion foreground ODIN/Sentry visible durante servicio activo.
- Tras detener/revocar: `ODIN_NOTIFICATION_RECORDS=NONE`.

Resultado detencion Sentry:
- Boton `Detener` ejecutado desde la app.
- Log observado: `Destruyendo SentryService...`.
- UI vuelve a estado detenido/listo segun consentimiento.

Resultado dumpsys services tras detener:
- `dumpsys activity services com.odin.core`: `(nothing)`.

Resultado appops:
- `POST_NOTIFICATION: allow` ya concedido por el entorno.
- `START_FOREGROUND: allow` registrado por la prueba explicita de Sentry.
- No aparece `SYSTEM_ALERT_WINDOW` concedido.
- No aparece `MANAGE_EXTERNAL_STORAGE` concedido.

Regresiones Dashboard:
- Dashboard abre.
- Muestra estado conservador y honesto.
- Captura: `fase7_dashboard_regression.png`.

Regresiones Herramientas:
- Herramientas abre.
- Accesos seguros no cambian consentimientos ni permisos.
- Captura: `fase7_tools_regression.png`.

Regresiones Red/NetShield:
- Red abre.
- No inicia VPN.
- Muestra que no hay metricas simuladas ni trafico inventado.
- Captura: `fase7_network_regression.png`.

Regresiones Logs:
- Logs abre sin crash.
- Captura: `fase7_logs_regression.png`.

Regresiones Ajustes:
- Ajustes abre sin spinner infinito.
- Persisten toggles legacy que deben auditarse en fase futura.
- Captura: `fase7_settings_regression.png`.

Regresiones Firewall:
- Firewall sigue bloqueado.
- No se habilitan acciones incompletas.
- Captura: `fase7_firewall_regression.png`.

Capturas generadas:
- `fase7_sentry_requires_consent.png`
- `fase7_sentry_ready.png`
- `fase7_sentry_active.png`
- `fase7_sentry_notification.png`
- `fase7_sentry_stopped.png`
- `fase7_sentry_revoked.png`
- `fase7_services_status.png`
- `fase7_dashboard_regression.png`
- `fase7_permissions_regression.png`
- `fase7_network_regression.png`
- `fase7_firewall_regression.png`
- `fase7_logs_regression.png`
- `fase7_settings_regression.png`
- `fase7_tools_regression.png`

Crashes encontrados:
- Crash de app: ninguno.
- Error funcional corregido: fallo AES-GCM por IV manual con clave Keystore de IV aleatorio requerido.
- Logcat estrecho sobre `AndroidRuntime`, `SentryService`, `SentryCore` y `CryptographicManager`: sin coincidencias de fatal, SecurityException, ForegroundServiceStartNotAllowedException ni error de IV.

Riesgos restantes:
- Ajustes mantiene toggles legacy con apariencia de accion real; deben auditarse antes de produccion.
- NetShield/VPN queda fuera de Fase 7 y debe validarse en Fase 8 antes de permitir activacion.
- BootReceiver no se probo con reinicio fisico real; queda validado por lectura, guards de codigo y no autoarranque en instalacion limpia.
- El dispositivo tiene comportamiento MIUI/AppOps propio; conviene repetir en Android AOSP/Pixel para compatibilidad de Play.

Fase 7 queda cerrada al 100%?: Si.

Siguiente fase oficial recomendada:
- Fase 8: NetShield / VPN local con consentimiento, autorizacion VPN, foreground notification, start/stop explicito, no trafico simulado y validacion real en dispositivo.

## Fase 7.1 - Verificacion final pre-Fase 8

Estado:
- Verificacion final ejecutada el 2026-07-02 en dispositivo real `116829e90703`.
- No se implementaron funciones nuevas.
- No se modificaron UI, permisos, Manifest, NetShield/VPN ni crypto.
- Solo se actualizo este reporte con la evidencia de cierre.

Archivos revisados:
- `FASE_7_SENTRYCORE_FOREGROUND_SERVICE.md`
- `app/src/main/java/com/odin/core/security/CryptographicManager.java`
- `app/src/main/java/com/odin/core/persistence/SentryCore.java`
- `app/src/main/java/com/odin/core/service/SentryService.java`
- `app/src/main/java/com/odin/core/receiver/BootReceiver.java`
- `app/src/main/java/com/odin/core/sentry/SentryController.kt`
- `app/src/main/java/com/odin/core/ui/screens/network/NetworkScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/tools/ToolsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/firewall/FirewallScreen.kt`

Cambio crypto verificado:
- El cambio AES-GCM / Android Keystore ya estaba documentado en este reporte.
- `CryptographicManager.encrypt()` usa `cipher.init(Cipher.ENCRYPT_MODE, masterKey)` y obtiene el IV mediante `cipher.getIV()`.
- No queda IV manual en cifrado con Keystore.
- `GCMParameterSpec` se usa en descifrado con el IV almacenado junto al ciphertext, que es el flujo correcto.
- No se encontraron claves criptograficas hardcodeadas; `KEYSTORE_ALIAS = "odin_master_key"` es un alias de Keystore, no una clave secreta.
- No se encontro `SecretKeySpec` ni material de clave embebido en `CryptographicManager` o `SentryCore`.
- Si cifrado falla, `SentryCore.appendEvent()` marca `isHealthy=false` y lanza `PersistenceException`; no guarda ni devuelve texto sensible en claro.
- Si descifrado de eventos falla, `SentryCore.loadAllEvents()` intenta autosanacion y devuelve lista vacia; no hace fallback a plaintext.

Validacion compileDebugKotlin:
- Comando: `.\gradlew.bat :app:compileDebugKotlin --console=plain --no-daemon`
- Resultado: `BUILD SUCCESSFUL`.

Validacion assembleDebug:
- Comando: `.\gradlew.bat :app:assembleDebug --console=plain --no-daemon`
- Resultado: `BUILD SUCCESSFUL`.

Resultado instalacion:
- `adb devices`: `116829e90703 device`.
- `pm uninstall com.odin.core`: `Success`.
- `adb push app\build\outputs\apk\debug\app-debug.apk /data/local/tmp/odin-app-debug.apk`: correcto.
- `pm install -r /data/local/tmp/odin-app-debug.apk`: `Success`.

Resultado apertura:
- App abierta con `adb shell monkey -p com.odin.core 1`.
- `dumpsys activity services com.odin.core` tras abrir: `(nothing)`.
- No inicio `SentryService`.
- No inicio `NetShieldVpnService`.
- No inicio foreground service sin accion del usuario.

Resultado logcat crypto:
- Filtro estrecho sobre `AndroidRuntime`, `SentryService`, `SentryCore` y `CryptographicManager`: sin coincidencias de `FATAL EXCEPTION`, `SecurityException`, `KeyStoreException`, `AEADBadTagException`, `BadPaddingException`, `IllegalBlockSizeException` ni `InvalidAlgorithmParameterException`.
- El filtro amplio del sistema mostro ruido externo de MIUI/otras apps; no se asocio a crash de ODIN.
- `uiautomator dump` sigue emitiendo `FileNotFoundException` de MIUI para `/data/system/theme_config/theme_compatibility.xml`; es externo al APK y no corresponde a crash de ODIN.

Resultado inicio Sentry:
- Consentimiento principal aceptado desde pantalla Sentry.
- `dumpsys` inmediatamente despues de aceptar consentimiento: `(nothing)`, confirmando que consentimiento no inicia servicio.
- Boot consent permanecio `Desactivado`.
- Inicio ejecutado solo desde boton `Iniciar Sentry`.
- `dumpsys activity services com.odin.core` mostro `com.odin.core/.service.SentryService`.
- `isForeground=true`, `foregroundId=8901`, canal `odin_sentry_channel`.
- Logcat Sentry: `SentryService inicializado exitosamente`.
- Sin errores crypto durante inicio.

Resultado detencion Sentry:
- Boton `Detener` ejecutado desde la app.
- Logcat Sentry: `Destruyendo SentryService...`.
- `dumpsys activity services com.odin.core` despues de detener: `(nothing)`.
- `dumpsys notification`: `ODIN_NOTIFICATION_RECORDS=NONE`.

Resultado revocacion consentimiento:
- `Revocar consentimiento` ejecutado desde pantalla Sentry.
- UI volvio a `Requiere consentimiento`.
- `dumpsys activity services com.odin.core`: `(nothing)`.
- `dumpsys notification`: `ODIN_NOTIFICATION_RECORDS=NONE`.

Resultado NetShield/VPN no activado:
- No se toco NetShield/VPN durante la prueba.
- `dumpsys activity services com.odin.core` final: `(nothing)`.
- `NetworkScreen.kt` mantiene texto: `ODIN no inicia VPN ni solicita autorizacion desde esta pantalla`.
- `NetworkScreen.kt` mantiene VPN local con estado `No activa`.
- `ServicesStatusScreen.kt` mantiene `NetShieldVpnService` como `Requiere autorizacion`.

Resultado Firewall regression:
- `ToolsScreen.kt` y `NetworkScreen.kt` mantienen `Firewall avanzado` como `Bloqueado`.
- `NetworkScreen.kt` informa que la creacion, edicion y activacion de reglas sigue bloqueada.
- En instalacion limpia no hay reglas activas ni servicio de firewall.
- Riesgo observado sin modificar: `FirewallScreen.kt` conserva codigo legacy de create/update/delete en dialogos y `IconButton` de delete para reglas existentes; aunque el boton de crear esta deshabilitado y el texto indica bloqueo, esta pantalla debe cerrarse completamente en una fase posterior antes de produccion.

Crashes encontrados:
- Crash de ODIN: ninguno.
- Crash Hilt: ninguno.
- Crash crypto: ninguno.
- `ForegroundServiceStartNotAllowedException`: no observado.

Riesgos restantes:
- `FirewallScreen.kt` mantiene rutas legacy de create/update/delete que deben bloquearse de forma mas fuerte en una fase posterior.
- Ajustes conserva toggles legacy con apariencia accionable y deben auditarse antes de produccion.
- BootReceiver no se valido con reinicio fisico real; se mantiene protegido por doble consentimiento a nivel codigo y no arranco en instalacion limpia.
- Validacion ejecutada sobre dispositivo MIUI; conviene repetir en Android AOSP/Pixel antes de release.

Fase 7 queda definitivamente cerrada para avanzar a Fase 8?: Si.

Recomendacion:
- Iniciar Fase 8 oficial: NetShield / VPN local, manteniendo el mismo criterio de consentimiento explicito, servicio visible y cero metricas simuladas.
