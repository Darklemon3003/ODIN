# ODIN - Fase 2: Cumplimiento, permisos y consentimiento

Fecha: 2026-06-30

## Estado

Fase 2 cerrada a nivel codigo + build debug.

Runtime en dispositivo real queda pendiente: `adb` existe en el SDK local, pero no hay dispositivos listados.

## Archivos revisados

- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/odin/core/ui/MainActivity.kt`
- `app/src/main/java/com/odin/core/receiver/BootReceiver.java`
- `app/src/main/java/com/odin/core/service/SentryService.java`
- `app/src/main/java/com/odin/core/service/NetShieldVpnService.java`
- `app/src/main/java/com/odin/core/ui/screens/dashboard/DashboardScreen.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/DashboardViewModel.kt`
- `app/src/main/java/com/odin/core/data/repository/SecurityRepositoryImpl.kt`
- `app/src/main/java/com/odin/core/ui/screens/firewall/FirewallScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/quarantine/QuarantineScreen.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/QuarantineViewModel.kt`
- `app/build.gradle`
- `build.gradle`

## Archivos modificados

- `build.gradle`
- `app/build.gradle`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/odin/core/OdinApplication.kt`
- `app/src/main/java/com/odin/core/ui/MainActivity.kt`
- `app/src/main/java/com/odin/core/receiver/BootReceiver.java`
- `app/src/main/java/com/odin/core/ui/viewmodel/DashboardViewModel.kt`
- `app/src/main/java/com/odin/core/data/repository/SecurityRepositoryImpl.kt`
- `app/src/main/java/com/odin/core/ui/screens/firewall/FirewallScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/quarantine/QuarantineScreen.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/QuarantineViewModel.kt`
- `FASE_2_CUMPLIMIENTO_PERMISOS_CONSENTIMIENTO.md`

## Cambios realizados

1. Se agrego `OdinApplication` con `@HiltAndroidApp`.
2. Se declaro `android:name=".OdinApplication"` en el manifest.
3. Se normalizo Gradle a Plugin DSL para AGP, Kotlin, KAPT y Hilt en el mismo scope.
4. Se evito que `MainActivity.onCreate()` solicite permisos o arranque `SentryService` automaticamente.
5. Se controlo `BootReceiver` con la preferencia `consent_boot_sentry_enabled`, default `false`.
6. Se corrigio `DashboardViewModel` para leer una muestra de cada flujo con `first()` y no bloquear loading.
7. Se cambiaron estados base del dashboard a no validado: `isSecure=false`, componentes inactivos, `postureScore=0`, `edrHealth=0`.
8. Se deshabilito creacion, edicion y toggle de reglas en Firewall UI.
9. Se deshabilito eliminar/mantener en Quarantine UI y se dejo de cargar behavior data no soportada.

## Por que se cambiaron

- Hilt estaba incompleto: `MainActivity` usaba `@AndroidEntryPoint` sin `@HiltAndroidApp`.
- Al corregir Hilt, aparecio un conflicto real de plugin classpath con JavaPoet; Gradle legacy ocultaba el problema.
- ODIN no debe pedir permisos sensibles ni activar servicios defensivos sin accion clara del usuario.
- Boot auto-start sin consentimiento puede parecer spyware/stalkerware y es riesgoso para Google Play.
- Dashboard no debe afirmar que el sistema esta protegido si usa datos iniciales no verificados.
- Firewall y Quarantine exponian acciones que llamaban operaciones no soportadas.

## Validaciones ejecutadas

- `where.exe adb`: no encontro `adb` en PATH.
- Busqueda SDK local: `C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe` encontrado.
- `adb devices`: sin dispositivos listados.
- `.\gradlew.bat :app:compileDebugKotlin --console=plain --no-daemon`: OK.
- `.\gradlew.bat :app:assembleDebug --console=plain --no-daemon`: OK.
- APK generado: `app/build/outputs/apk/debug/app-debug.apk`.

## Resultado runtime en dispositivo

Pendiente. No hay dispositivo conectado por ADB.

No se ejecuto:

- `adb install -r app\build\outputs\apk\debug\app-debug.apk`
- `adb shell monkey -p com.odin.core 1`
- `adb logcat -d | findstr /i "FATAL EXCEPTION odin AndroidRuntime Hilt"`

## Resultado Hilt/Application

Corregido a nivel build:

- `OdinApplication` existe y tiene `@HiltAndroidApp`.
- Manifest declara `android:name=".OdinApplication"`.
- `compileDebugKotlin` y `assembleDebug` pasan con Hilt activo.

## Matriz de permisos

| Permiso | Funcion que lo usa | Archivo relacionado | Justificacion tecnica | Riesgo Play/privacidad | Estado | Decision |
|---|---|---|---|---|---|---|
| `POST_NOTIFICATIONS` | Notificaciones foreground | `SentryService.java`, `NetShieldVpnService.java` | Necesario en Android 13+ para notificaciones visibles | Medio | Necesario pero requiere UX | Pedir progresivo, no en arranque |
| `FOREGROUND_SERVICE` | Servicios Sentry/NetShield/Tor/VPN | `AndroidManifest.xml` servicios | Necesario para foreground services | Medio | Necesario | Mantener con explicacion |
| `FOREGROUND_SERVICE_SPECIAL_USE` | Servicios declarados como `specialUse` | `AndroidManifest.xml` | Requerido por tipos actuales | Alto | Riesgoso | Revisar tipo exacto antes de Play |
| `SYSTEM_ALERT_WINDOW` | Overlay potencial | `MainActivity.kt` | No hay flujo funcional claro validado | Critico | Excesivo / pendiente | No pedir; candidato a eliminar |
| `RECEIVE_BOOT_COMPLETED` | BootReceiver | `BootReceiver.java` | Reanudar proteccion tras reinicio solo si usuario acepto | Alto | Requiere consentimiento explicito | Mantener controlado por flag |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Persistencia de servicio | `AndroidManifest.xml` | No se encontro flujo UX validado | Alto | Pendiente de validacion | Candidato a eliminar o pedir manual |
| `BIND_VPN_SERVICE` | VPN services | `NetShieldVpnService.java`, `VpnAnonymityService.java` | Debe estar como permission del service VPN | Medio/alto | Uso en service justificado; uses-permission dudoso | Revisar eliminar `uses-permission` |
| `VIBRATE` | Notificaciones/alertas | Manifest/customization | Baja sensibilidad | Bajo | Pendiente | Mantener solo si se usa |
| `USE_FULL_SCREEN_INTENT` | Alertas criticas | `AndroidManifest.xml` | No se encontro uso real | Alto | No usado | Candidato a eliminar |
| `INTERNET` | Red, DNS, VPN, reportes locales de red | Modulos network | Necesario para funciones de red | Medio | Necesario | Mantener |
| `ACCESS_NETWORK_STATE` | Estado de red | Modulos network | Necesario para diagnostico/red | Bajo/medio | Necesario | Mantener |
| `CHANGE_NETWORK_STATE` | Cambios de red | Manifest | No se encontro flujo claro | Medio | Pendiente de validacion | Candidato a eliminar |
| `READ_EXTERNAL_STORAGE` | Analisis de archivos legacy | File analysis | Legacy hasta Android 12 | Alto | Riesgoso | Migrar a SAF/granular |
| `WRITE_EXTERNAL_STORAGE` | Cuarentena/archivos legacy | File analysis | Ignorado/restringido en Android moderno | Alto | Excesivo | Candidato a eliminar |
| `MANAGE_EXTERNAL_STORAGE` | Analisis amplio de archivos | File analysis | Solo justificable para all files access | Critico | Excesivo | Evitar salvo caso Play muy justificado |
| `READ_MEDIA_IMAGES` | Analisis de archivos multimedia | File analysis | Android 13+ media granular | Alto | Necesario solo si funcion real | Pedir por accion del usuario |
| `READ_MEDIA_VIDEO` | Analisis de archivos multimedia | File analysis | Android 13+ media granular | Alto | Necesario solo si funcion real | Pedir por accion del usuario |
| `READ_MEDIA_AUDIO` | Analisis de archivos multimedia | File analysis | Android 13+ media granular | Alto | Necesario solo si funcion real | Pedir por accion del usuario |

## Permisos necesarios

- `FOREGROUND_SERVICE`
- `POST_NOTIFICATIONS`
- `INTERNET`
- `ACCESS_NETWORK_STATE`
- `BIND_VPN_SERVICE` como permiso del service VPN

## Permisos riesgosos

- `FOREGROUND_SERVICE_SPECIAL_USE`
- `SYSTEM_ALERT_WINDOW`
- `RECEIVE_BOOT_COMPLETED`
- `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`
- `USE_FULL_SCREEN_INTENT`
- `MANAGE_EXTERNAL_STORAGE`
- `READ_MEDIA_*`
- `READ_EXTERNAL_STORAGE`
- `WRITE_EXTERNAL_STORAGE`

## Permisos candidatos a eliminar

- `SYSTEM_ALERT_WINDOW`, si no existe funcion overlay real y explicada.
- `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`, si no existe onboarding manual.
- `USE_FULL_SCREEN_INTENT`, si no hay alerta critica real.
- `CHANGE_NETWORK_STATE`, si no se valida uso real.
- `WRITE_EXTERNAL_STORAGE`, por obsoleto/restringido.
- `MANAGE_EXTERNAL_STORAGE`, salvo justificacion fuerte para Google Play.
- `BIND_VPN_SERVICE` como `uses-permission`; debe revisarse porque el service ya declara `android:permission`.

## Funciones sensibles que requieren consentimiento

- Inicio de Sentry foreground service.
- Reactivacion tras boot.
- Inicio de NetShield VPN.
- Cualquier solicitud de overlay.
- Solicitud de all files access o permisos media.
- Exportacion de logs.
- Cuarentena/destruccion/restauracion de archivos o apps.

## Funciones bloqueadas u ocultadas por estar incompletas

- Firewall: crear, editar y activar/desactivar reglas.
- Quarantine: eliminar y mantener.
- Quarantine: behavior data.
- Dashboard: estado inicial protegido; ahora queda no validado/inactivo.

## Riesgos restantes

- Falta validacion real en dispositivo.
- Manifest aun contiene permisos sensibles que deben depurarse en la siguiente fase.
- NetShield VPN no esta probado con autorizacion real ni trafico real.
- `FOREGROUND_SERVICE_SPECIAL_USE` debe justificarse o reemplazarse antes de Play.
- Hay activities exportadas que requieren revision de superficie externa.
- Iconos/notificaciones profesionales quedan fuera de esta fase.

## Siguiente fase recomendada

Fase 3: permisos progresivos y pantalla de consentimiento.

Orden sugerido:

1. Crear modelo central de consentimiento local.
2. Crear pantalla simple de permisos/consentimiento sin redisenar visualmente.
3. Conectar Sentry, BootReceiver, NetShield y almacenamiento a ese modelo.
4. Reemplazar permisos de storage amplios por SAF o flujos granulares.
5. Probar instalacion y arranque en dispositivo real con ADB.
