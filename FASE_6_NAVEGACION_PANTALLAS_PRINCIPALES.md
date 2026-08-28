# FASE 6 - Navegacion y pantallas principales

## Fase
Fase 6 oficial: Navegacion y pantallas principales.

## Estado
Implementada a nivel codigo y build. No cerrada al 100% porque ADB no detecta dispositivo real.

## Objetivo
Organizar ODIN como una aplicacion navegable, profesional y honesta, sin activar funciones sensibles, servicios, VPN, permisos ni consentimientos.

## Diagnostico previo
- Navegacion actual encontrada: `MainActivity.kt` usa Compose con `NavHost` y bottom navigation.
- Bottom navigation previa: Dashboard, Logs, Threats, Firewall y Settings.
- Pantallas Compose existentes: Dashboard, Logs, Settings, Firewall, Quarantine y Threats.
- Pantallas faltantes para producto: Herramientas, Red/NetShield, Permisos, Estado de servicios y Acerca de ODIN.
- Pantallas legacy XML detectadas: `FileAnalysisActivity` y `NetworkAnonymityActivity`.
- Decision de Fase 6: no lanzar pantallas legacy desde navegacion principal hasta auditar sus permisos, UX y flujos sensibles.
- Riesgo observado: Firewall mantiene pantalla legacy Compose con acciones parcialmente bloqueadas; queda fuera de la barra principal.
- Riesgo observado: Settings aun contiene toggles legacy de configuracion; se conserva sin redisenar en esta fase.

## Archivos revisados
- `app/src/main/java/com/odin/core/ui/MainActivity.kt`
- `app/src/main/java/com/odin/core/ui/navigation/Screen.kt`
- `app/src/main/java/com/odin/core/ui/navigation/BottomNavigationBar.kt`
- `app/src/main/java/com/odin/core/ui/navigation/OdinNavigation.kt`
- `app/src/main/java/com/odin/core/ui/screens/dashboard/DashboardScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/logs/LogsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/settings/SettingsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/firewall/FirewallScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/quarantine/QuarantineScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/threats/ThreatsScreen.kt`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/res/layout/activity_file_analysis.xml`
- `app/src/main/res/layout/activity_network_anonymity.xml`
- `app/src/main/java/com/odin/core/security/ConsentManager.java`
- `app/src/main/java/com/odin/core/ui/viewmodel/LogsViewModel.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/QuarantineViewModel.kt`

## Archivos modificados
- `app/src/main/java/com/odin/core/ui/MainActivity.kt`
- `app/src/main/java/com/odin/core/ui/navigation/Screen.kt`
- `app/src/main/java/com/odin/core/ui/navigation/BottomNavigationBar.kt`

## Archivos creados
- `app/src/main/java/com/odin/core/ui/screens/tools/ToolsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/network/NetworkScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/permissions/PermissionsScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/services/ServicesStatusScreen.kt`
- `app/src/main/java/com/odin/core/ui/screens/about/AboutScreen.kt`
- `FASE_6_NAVEGACION_PANTALLAS_PRINCIPALES.md`

## Rutas creadas o ajustadas
- `dashboard`: Inicio.
- `tools`: Herramientas.
- `network`: Red.
- `logs`: Logs.
- `settings`: Ajustes.
- `permissions`: Permisos.
- `services_status`: Estado de servicios.
- `about`: Acerca de ODIN.
- `threats`, `firewall` y `quarantine`: se conservan como rutas internas legacy/regresion, no como secciones principales.

## Bottom navigation
Nuevo orden:
1. Inicio.
2. Herramientas.
3. Red.
4. Logs.
5. Ajustes.

## Pantallas implementadas
- Herramientas: categorias Proteccion, Privacidad, Sistema y Red.
- Red / NetShield: estado informativo, sin VPN automatica ni trafico simulado.
- Permisos: resumen informativo, sin solicitar permisos al abrir.
- Estado de servicios: lista informativa de servicios declarados, sin iniciar foreground services.
- Acerca de ODIN: proposito defensivo, consentimiento, privacidad y limites.

## Pantallas conservadas
- Dashboard profesional de Fase 5.
- Logs.
- Ajustes.
- Firewall como ruta interna de regresion.
- Quarantine como ruta interna.
- Threats como ruta interna.

## Pantallas no integradas todavia
- `FileAnalysisActivity`: existe como Activity XML legacy. No se abre desde Fase 6.
- `NetworkAnonymityActivity`: existe como Activity XML legacy. No se abre desde Fase 6.
- Detalle de eventos: existe como dialogo dentro de `LogsScreen`, no como ruta independiente.

## Datos reales usados
- Manifest y componentes declarados.
- Modelo central `ConsentManager`.
- Pantallas Compose existentes.
- Estado de funcionalidades ya clasificado en fases previas.

## Datos no disponibles
- Estado runtime confiable de SentryService.
- Estado runtime confiable de NetShieldVpnService.
- Trafico real.
- Amenazas bloqueadas reales.
- Porcentaje o score real de proteccion.
- Estado real de VPN autorizada/activa.

## Datos ocultados por no ser confiables
- No se muestran metricas de trafico.
- No se muestran porcentajes de proteccion.
- No se muestran amenazas bloqueadas.
- No se afirma que VPN, Firewall, SentryCore o NetShield esten activos.
- No se muestran textos absolutos como "Protegido" o "Sistema seguro".

## Funciones sensibles que NO se activan
- SentryService.
- NetShieldVpnService.
- VpnService.prepare().
- Foreground services.
- Setters de consentimiento.
- Solicitud de permisos Android.
- Acciones destructivas de cuarentena.
- Creacion, edicion o activacion de firewall.

## Acciones seguras disponibles
- Navegar a Permisos.
- Navegar a Estado de servicios.
- Navegar a Red.
- Navegar a Logs.
- Navegar a Ajustes.
- Navegar a Acerca de ODIN.
- Ver pantalla interna de Firewall como regresion/bloqueo.

## Acciones bloqueadas o diferidas
- Activar SentryCore.
- Activar NetShield/VPN.
- Lanzar flujo VPN.
- Solicitar notificaciones.
- Solicitar acceso a archivos.
- Abrir FileAnalysisActivity desde Herramientas.
- Abrir NetworkAnonymityActivity desde Red.
- Habilitar Firewall como funcionalidad completa.
- Acciones destructivas de Quarantine.

## Validacion estatica de seguridad UI
Comando ejecutado:

```powershell
rg -n "Protegido|Sistema seguro|100% seguro|Amenazas bloqueadas|VPN segura|Firewall activo|startService|startForegroundService|VpnService\.prepare|requestPermissions|set[A-Za-z]+Enabled\(true" app/src/main/java/com/odin/core/ui/MainActivity.kt app/src/main/java/com/odin/core/ui/navigation app/src/main/java/com/odin/core/ui/screens/tools app/src/main/java/com/odin/core/ui/screens/network app/src/main/java/com/odin/core/ui/screens/permissions app/src/main/java/com/odin/core/ui/screens/services app/src/main/java/com/odin/core/ui/screens/about app/src/main/java/com/odin/core/ui/screens/dashboard -S
```

Resultado: sin coincidencias.

## Validacion compileDebugKotlin
Comando:

```powershell
.\gradlew.bat :app:compileDebugKotlin --console=plain --no-daemon
```

Resultado: `BUILD SUCCESSFUL`.

Nota: el primer intento excedio el timeout del wrapper de ejecucion, pero el proceso Gradle termino. Se repitio el comando y paso correctamente.

## Validacion assembleDebug
Comando:

```powershell
.\gradlew.bat :app:assembleDebug --console=plain --no-daemon
```

Resultado: `BUILD SUCCESSFUL`.

APK generado:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Tamano observado: `99123987` bytes.

## Resultado ADB
Comando:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' devices
```

Resultado:

```text
List of devices attached
```

Sin dispositivos visibles.

Se ejecuto reinicio de ADB:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' kill-server
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' start-server
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' devices
```

Resultado final: lista vacia.

## Resultado instalacion
No ejecutado. Motivo: `adb devices` no muestra dispositivo en estado `device`.

## Resultado apertura
No ejecutado. Motivo: no hay dispositivo visible por ADB.

## Resultado logcat
No ejecutado. Motivo: no hay dispositivo visible por ADB.

## Resultado servicios
No ejecutado. Motivo: no hay dispositivo visible por ADB.

## Resultado appops
No ejecutado. Motivo: no hay dispositivo visible por ADB.

## Validacion visual manual
No ejecutada. Motivo: no hay dispositivo visible por ADB.

## Capturas generadas
No generadas por bloqueo ADB.

Capturas pendientes:
- `fase6_dashboard_regression.png`
- `fase6_tools.png`
- `fase6_network.png`
- `fase6_logs.png`
- `fase6_settings.png`
- `fase6_permissions.png`
- `fase6_services_status.png`
- `fase6_about.png`
- `fase6_firewall_regression.png`

## Crashes encontrados
No se detectaron crashes en build. Runtime real no validado por bloqueo ADB.

## Problemas visuales encontrados
No evaluados en dispositivo real por bloqueo ADB.

## Riesgos criticos restantes
- No se puede cerrar Fase 6 al 100% sin instalacion, apertura, logcat, servicios, appops y capturas en dispositivo real.

## Riesgos altos restantes
- Settings conserva toggles legacy que deben revisarse en una fase posterior para no comunicar activacion sensible sin flujo completo.
- Firewall conserva pantalla interna legacy; se retiro de la barra principal, pero debe auditarse antes de habilitar acciones reales.
- Activities XML legacy de red y analisis de archivos no se integraron hasta auditar permisos, consentimiento y estabilidad.

## Riesgos aceptados para fases futuras
- Detalle de eventos sigue como dialogo dentro de Logs, no como ruta separada.
- Red/NetShield queda informativo, sin flujo VPN.
- SentryCore queda informativo, sin control de activacion.

## Criterio de cierre Fase 6
- `compileDebugKotlin`: cumple.
- `assembleDebug`: cumple.
- APK generado: cumple.
- Dispositivo ADB `device`: no cumple.
- Instalacion real: pendiente.
- Apertura real: pendiente.
- Logcat sin crash: pendiente.
- Servicios no iniciados al abrir: pendiente.
- AppOps sin permisos concedidos automaticamente: pendiente.
- Capturas: pendiente.

## Fase 6 queda cerrada al 100%?
No.

## Siguiente accion oficial recomendada
Recuperar conexion ADB y repetir validacion runtime completa:

1. Revisar cable USB.
2. Cambiar puerto USB.
3. Cambiar modo USB a Transferencia de archivos.
4. Activar Depuracion USB.
5. Revocar autorizaciones USB si no aparece dialogo RSA.
6. Desconectar y conectar de nuevo.
7. Aceptar huella RSA.
8. Reintentar:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' devices
```

Solo cuando aparezca un dispositivo en estado `device`, continuar con instalacion limpia, apertura, logcat, servicios, appops y capturas.

---

## Cierre runtime real - Fase 6

## Fase
Fase 6 oficial: Navegacion y pantallas principales.

## Estado
Cerrada a nivel runtime real en dispositivo fisico.

## Dispositivo
`116829e90703`

## Resultado adb devices
Comando:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' devices
```

Resultado:

```text
List of devices attached
116829e90703    device
```

## Resultado compileDebugKotlin
Comando:

```powershell
.\gradlew.bat :app:compileDebugKotlin --console=plain --no-daemon
```

Resultado: exitoso, exit code `0`.

## Resultado assembleDebug
Comando:

```powershell
.\gradlew.bat :app:assembleDebug --console=plain --no-daemon
```

Resultado: `BUILD SUCCESSFUL`.

## Resultado instalacion
Desinstalacion limpia:

```text
Success
```

Instalacion robusta:

```text
app\build\outputs\apk\debug\app-debug.apk: 1 file pushed, 0 skipped. 34.9 MB/s (99123987 bytes in 2.710s)
Success
```

APK instalado:

```text
package:/data/app/~~JyjhPTqJkIXmakY3Tzwe7A==/com.odin.core-1zygH-vObvpYsQuwwELlSQ==/base.apk
```

## Resultado apertura
Comando:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' shell monkey -p com.odin.core 1
```

Resultado:

```text
Events injected: 1
```

La app abrio en `com.odin.core/.ui.MainActivity`.

## Resultado logcat
Filtro amplio solicitado ejecutado:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' logcat -d | findstr /i "FATAL EXCEPTION odin AndroidRuntime Hilt SecurityException Resources NotFoundException InflateException Compose"
```

Resultado: sin `FATAL EXCEPTION`, sin crash Hilt, sin `Resources.NotFoundException`, sin `InflateException`, sin `SecurityException` de ODIN y sin crash Compose.

Filtro estricto adicional:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' logcat -d | findstr /i /c:"FATAL EXCEPTION" /c:"Hilt" /c:"SecurityException" /c:"Resources NotFoundException" /c:"InflateException" /c:"AndroidRuntime" | findstr /i "com.odin.core odin"
```

Resultado: sin coincidencias, exit code `1`.

## Resultado servicios
Comando:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' shell dumpsys activity services com.odin.core
```

Resultado:

```text
ACTIVITY MANAGER SERVICES (dumpsys activity services)
  (nothing)
```

Validacion:
- No SentryService activo al abrir.
- No NetShieldVpnService activo al abrir.
- No foreground services iniciados automaticamente.

## Resultado appops
Comando:

```powershell
& 'C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe' shell appops get com.odin.core
```

Resultado relevante:

```text
POST_NOTIFICATION: allow
MIUIOP(10017): ask
```

Validacion adicional con `dumpsys package`:

```text
requested permissions:
  android.permission.POST_NOTIFICATIONS
runtime permissions:
```

Interpretacion:
- `SYSTEM_ALERT_WINDOW` no aparece concedido.
- `MANAGE_EXTERNAL_STORAGE` no aparece concedido.
- No aparecen permisos runtime peligrosos concedidos en `dumpsys package`.
- `POST_NOTIFICATION: allow` aparece en AppOps de MIUI, pero no hubo dialogo de permiso al abrir y `runtime permissions` no lista permisos concedidos.

## Validacion visual Dashboard
Cumple:
- Abre sin crash.
- No muestra "Protegido" sin evidencia.
- No muestra "Sistema seguro".
- No muestra datos falsos.
- No activa servicios.
- Muestra estado honesto: `Requiere atencion`, `Postura: No disponible`, `Alertas locales: 0`.

Captura: `fase6_dashboard_regression.png`.

## Validacion visual Herramientas
Cumple:
- Abre sin crash.
- Organizada por categorias.
- Funciones parciales y bloqueadas aparecen como `Requiere consentimiento`, `Legacy parcial`, `Bloqueado`, `Parcial` o `Planeado`.
- No finge funciones inexistentes como activas.

Captura: `fase6_tools.png`.

## Validacion visual Red
Cumple:
- Abre sin crash.
- No inicia VPN.
- No muestra trafico falso.
- Indica `Requiere configuracion`, `No activa`, `Sin datos`, `Bloqueado` y `Planeado`.
- Explica limites de privacidad y no descifra trafico TLS.

Captura: `fase6_network.png`.

## Validacion visual Logs
Cumple parcialmente:
- Abre sin crash.
- No muestra pantalla blanca.
- No exporta datos automaticamente.
- Si no hay eventos, la lista queda vacia sin crash. Se recomienda mejorar el empty state visual en una fase posterior.

Captura: `fase6_logs.png`.

## Validacion visual Ajustes
Cumple:
- Abre sin spinner infinito.
- No pide permisos al abrir.
- No inicia servicios al abrir.
- No cambia consentimientos automaticamente durante la apertura.

Riesgo observado:
- Algunos toggles legacy aparecen activos por configuracion local y pueden comunicar capacidad mas fuerte de la que esta validada. Debe auditarse en fase posterior de Ajustes/consentimiento.

Captura: `fase6_settings.png`.

## Validacion visual Permisos
Cumple:
- Accesible desde Herramientas.
- Abre correctamente.
- No solicita permisos automaticamente.
- Muestra permisos de forma informativa.

Captura: `fase6_permissions.png`.

## Validacion visual Estado de servicios
Cumple:
- Accesible desde Herramientas.
- Abre correctamente.
- Muestra SentryService como `No validado`.
- Muestra NetShieldVpnService como `Requiere autorizacion`.
- No inicia ni detiene servicios.

Captura: `fase6_services_status.png`.

## Validacion visual Acerca de ODIN
Cumple:
- Accesible desde Herramientas.
- Abre correctamente.
- Comunica enfoque defensivo, transparencia, privacidad local, consentimiento y cumplimiento Android.

Captura: `fase6_about.png`.

## Validacion Firewall regression
Cumple:
- Accesible desde Red como pantalla interna de regresion.
- Sigue indicando que creacion, edicion y activacion de reglas estan deshabilitadas.
- El boton de crear regla aparece deshabilitado.
- No se ejecuto create/edit/toggle.

Captura: `fase6_firewall_regression.png`.

## Capturas generadas
- `fase6_dashboard_regression.png`
- `fase6_tools.png`
- `fase6_network.png`
- `fase6_logs.png`
- `fase6_settings.png`
- `fase6_permissions.png`
- `fase6_services_status.png`
- `fase6_about.png`
- `fase6_firewall_regression.png`

Captura auxiliar generada:
- `fase6_network_scrolled_for_firewall.png`

## Crashes encontrados
Ninguno de ODIN.

## Pantallas no accesibles
Ninguna de las pantallas obligatorias de Fase 6 quedo inaccesible.

## Correcciones aplicadas despues de validacion
Ninguna. No se hicieron cambios de codigo durante el cierre runtime real.

## Riesgos restantes
- `LogsScreen` deberia mostrar un empty state visual explicito cuando no hay eventos.
- `SettingsScreen` conserva toggles legacy que pueden verse como funciones activas aunque no se hayan validado como servicios reales.
- `POST_NOTIFICATION: allow` aparece en AppOps de MIUI; `dumpsys package` no lista runtime permissions concedidos y no hubo dialogo al abrir.
- Se observaron advertencias de rendimiento/slow traversal de MIUI, sin crash. Conviene optimizar composables en fases futuras si se repiten.

## Fase 6 queda cerrada al 100%?
Si.

## Siguiente fase oficial recomendada
Fase 7 oficial: flujo seguro de SentryCore/SentryService con consentimiento explicito, permisos, controles de activacion/detencion, notificacion foreground profesional y validacion runtime real.
