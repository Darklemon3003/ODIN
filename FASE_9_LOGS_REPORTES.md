# Fase 9 - Logs y Reportes

## Fase
Fase 9 oficial, subfase 9.1: visualizacion segura de eventos reales, empty state, filtros basicos y detalle sanitizado.

## Estado
Fase 9.1 implementada y validada en dispositivo real. Fase 9.2 implementada, validada en dispositivo real y cerrada con GO ABSOLUTE. La Fase 9 completa queda cerrada al 100%.

## Archivos revisados
- `app/src/main/java/com/odin/core/domain/model/LogEvent.kt`
- `app/src/main/java/com/odin/core/domain/repository/LogsRepository.kt`
- `app/src/main/java/com/odin/core/data/repository/LogsRepositoryIntegration.kt`
- `app/src/main/java/com/odin/core/data/repository/LogsRepositoryImpl.kt`
- `app/src/main/java/com/odin/core/ui/viewmodel/LogsViewModel.kt`
- `app/src/main/java/com/odin/core/ui/screens/logs/LogsScreen.kt`
- `app/src/main/java/com/odin/core/persistence/SentryCore.java`
- `app/src/main/java/com/odin/core/forensics/LogExporter.java`

## Archivos modificados
- `app/src/main/java/com/odin/core/data/repository/LogsRepositoryIntegration.kt`
- `app/src/main/java/com/odin/core/persistence/SentryCore.java`
- `app/src/main/java/com/odin/core/ui/viewmodel/LogsViewModel.kt`
- `app/src/main/java/com/odin/core/ui/screens/logs/LogsScreen.kt`
- `app/src/main/java/com/odin/core/ui/model/LogEventUi.kt`
- `app/src/main/java/com/odin/core/ui/model/LogSanitizer.kt`

## Estado inicial logs
`LogsScreen` existia, pero era basico: no tenia empty state profesional, no sanitizaba metadata/mensajes antes de mostrar y dependia de `LogsRepositoryIntegration`, que iniciaba vacio.

## Fuente real de eventos
La fuente real usada ahora es `SentryCore.fetchRecentEvents(200)`, consumida desde `LogsRepositoryIntegration`. No se crean eventos mock ni placeholders.

## Repositorio usado por UI
La UI sigue usando `LogsRepository` via Hilt, con implementacion `LogsRepositoryIntegration`.

## Problema LogsRepositoryIntegration
Antes no leia eventos reales desde `SentryCore`; por eso Logs y Dashboard podian quedar sin eventos aunque existieran registros reales.

## Correccion aplicada
`LogsRepositoryIntegration` carga eventos reales desde `SentryCore`, limita a 200 eventos recientes, ordena por timestamp descendente y mantiene fallback vacio seguro si ocurre un error.

## Modelo LogEventUi
Se creo un modelo UI seguro con:
- `id`
- `timestamp`
- `formattedTime`
- `module`
- `severity`
- `title`
- `summary`
- `sanitizedDetails`
- `source`
- `hasDetails`
- `canExport`

## Sanitizacion
Se agrego `LogSanitizer` para ocultar tokens, secretos, bearer/auth headers, payload/body/raw/content, emails, rutas locales, URLs completas e IPs completas antes de mostrar detalle en UI.

## Pantalla Logs
La pantalla ahora usa estilo ODIN, header claro, conteo de eventos reales, buscador, filtros, exportacion bloqueada y lista de eventos sanitizados.

## Empty state
Validado con filtro sin coincidencias: la pantalla muestra `0 visibles` sin crear logs falsos ni crashear. En instalacion limpia aparecieron eventos reales de inicializacion de `SentryCore`, por lo que el empty state absoluto queda cubierto por filtros sin resultados.

## Filtros
Validado filtro por severidad `Info` y filtro/busqueda sin coincidencias. Los filtros no crashean y no inventan registros.

## Detalle de evento
Validado con evento real `Sentry core`: muestra modulo, severidad, fecha, fuente, resumen y detalle sanitizado. La UI declara que el raw no esta disponible para evitar exposicion de datos sensibles.

## Exportacion JSON
Bloqueada en UI. El codigo existente devuelve string y no entrega archivo real al usuario, por lo que no se anuncia como exportacion completada.

## Exportacion CSV
Bloqueada en UI por el mismo motivo: no hay flujo de archivo real, confirmacion y sanitizacion completa.

## Exportacion PDF
Bloqueada. No se llama PDF real a la salida existente tipo texto.

## Reportes
No se integraron reportes forenses con placeholders. Quedan fuera hasta tener datos reales y flujo seguro.

## Integracion Dashboard
Dashboard queda beneficiado por el repositorio corregido, pero no se modifico visualmente en esta subfase. No muestra alertas falsas ni amenazas inventadas.

## Eventos Sentry
Logs muestra eventos reales de inicializacion de `SentryCore`. Se probo inicio/detencion de Sentry con flujo validado; el servicio inicio y se detuvo correctamente, pero esos eventos de inicio/detencion no aparecieron en Logs tras recarga. Pendiente integrar esos eventos en la fuente consumida por `LogsRepositoryIntegration`.

## Eventos NetShield
No se activo NetShield durante esta subfase para evitar introducir una prueba de red innecesaria en Logs. La regresion de Red/NetShield confirmo que no se activo VPN ni `tun0`.

## Datos sensibles ocultados
La UI no muestra metadata cruda, payloads, tokens, rutas completas, URLs completas ni raw logs. El detalle usa salida sanitizada.

## Datos no disponibles
- Exportacion real a archivo.
- PDF real.
- Eventos visibles de inicio/detencion de Sentry.
- Eventos visibles de inicio/detencion de NetShield en esta subfase.
- Reportes agregados basados en eventos reales.

## Validacion compileDebugKotlin
Resultado: `BUILD SUCCESSFUL`.

Comando:
`.\gradlew.bat :app:compileDebugKotlin --console=plain --no-daemon`

## Validacion assembleDebug
Resultado: `BUILD SUCCESSFUL`.

Comando:
`.\gradlew.bat :app:assembleDebug --console=plain --no-daemon`

## Resultado instalacion
Dispositivo: `116829e90703 device`.

Instalacion validada con APK debug. La ruta robusta por `pm install` tuvo restriccion de instalacion USB en un intento previo del entorno, y la instalacion final paso con `adb install -r -t` / `adb install --no-streaming -r -t`.

## Resultado apertura
La app abrio en dispositivo real con `monkey -p com.odin.core 1`.

## Resultado logcat
Sin `FATAL EXCEPTION` de ODIN, sin crash Hilt, sin `Resources.NotFoundException`, sin `InflateException`, sin `SQLiteException`, sin errores Room y sin errores crypto/Keystore. Se observo ruido externo de MIUI/UIAutomator y PackageInstaller no asociado a crash de ODIN.

## Resultado servicios inicial
Al abrir ODIN no quedaron `SentryService`, `NetShieldVpnService` ni foreground services activos.

Resultado final: `NO_MATCH_SERVICES_FINAL`.

## Resultado logs instalacion limpia
Logs abre sin pantalla blanca y muestra eventos reales de `SentryCore`. No muestra eventos falsos.

## Resultado filtros
Filtros de severidad, busqueda y limpieza probados. Con busqueda sin coincidencias se obtuvo `0 visibles` sin crash.

## Resultado eventos reales
Eventos reales de inicializacion de `SentryCore` visibles. Eventos de start/stop de Sentry no visibles todavia tras recarga; queda como pendiente tecnico.

## Resultado detalle
Detalle sanitizado validado con evento real de `SentryCore`.

## Resultado exportacion
Exportacion bloqueada de forma honesta con mensaje: no disponible todavia hasta tener archivo real, confirmacion y sanitizacion completa.

## Resultado appops
`POST_NOTIFICATION: allow` y `START_FOREGROUND: allow` por prueba controlada de Sentry. No se concedio `SYSTEM_ALERT_WINDOW` ni `MANAGE_EXTERNAL_STORAGE`. No se pidieron permisos al abrir Logs.

## Regresiones Dashboard
Dashboard abre y no muestra alertas falsas ni datos inventados.

Captura: `fase9_dashboard_regression.png`.

## Regresiones Sentry
Sentry abre por flujo seguro, inicio/detencion funcionaron, y luego se revoco consentimiento. No quedo servicio activo.

Captura: `fase9_sentry_regression.png`.

## Regresiones NetShield
Red/NetShield abre en modo informativo. No se activo VPN, no quedo `tun0`, no hubo `NetShieldVpnService` residual.

Captura: `fase9_netshield_regression.png`.

## Regresiones Firewall
Firewall sigue bloqueado como funcion incompleta.

Captura: `fase9_firewall_regression.png`.

## Capturas generadas
- `fase9_logs_empty.png`
- `fase9_logs_with_events.png`
- `fase9_logs_filters.png`
- `fase9_log_detail.png`
- `fase9_export_blocked.png`
- `fase9_dashboard_regression.png`
- `fase9_sentry_regression.png`
- `fase9_netshield_regression.png`
- `fase9_firewall_regression.png`

## Crashes encontrados
Ningun crash de ODIN. No se detectaron `FATAL EXCEPTION`, Hilt crash, `SecurityException` de ODIN, `Resources.NotFoundException`, `InflateException`, Room/SQLite ni errores crypto.

## Riesgos restantes
- SentryService inicia/detiene correctamente, pero sus eventos de accion no aparecen todavia en Logs.
- NetShield no genero eventos visibles en esta subfase porque no se activo para pruebas de Logs.
- Exportacion real requiere una fase posterior con SAF/share intent, archivo real y sanitizacion.
- Eventos antiguos sin severity conservan fallback `INFO`; eventos nuevos preservan severity/metadata con serializacion compatible.

## Fase 9.1 queda cerrada al 100%
Si. Visualizacion segura, filtros, detalle sanitizado, empty state por filtro, exportacion bloqueada, build, instalacion y regresiones principales quedaron validados.

## Fase 9 queda cerrada al 100%
Si. Fase 9.2 completo la integracion real de eventos Sentry/NetShield, refresh robusto, exportacion JSON/CSV via SAF, PDF bloqueado, y validacion runtime completa en dispositivo real. Ver seccion Fase 9.2 abajo para evidencia detallada.

## Siguiente fase oficial recomendada
Fase 9.2: integrar eventos operativos reales de Sentry/NetShield en `SentryCore`/repositorio de logs sin tocar servicios sensibles salvo el punto minimo necesario, y definir exportacion segura con archivo real usando un flujo explicito.

---

# Fase 9.2 - Implementacion real, refresh robusto y exportacion segura

## Estado
Implementada a nivel codigo, build y validada en dispositivo real `116829e90703` (Xiaomi 21121119SG, Android 12, MIUI V130). Todas las pruebas runtime pasaron con evidencia real. La Fase 9 completa queda cerrada al 100% con GO ABSOLUTE.

## Archivos revisados
- `app/src/main/java/com/odin/core/ui/viewmodel/LogsViewModel.kt`
- `app/src/main/java/com/odin/core/ui/screens/logs/LogsScreen.kt`
- `app/src/main/java/com/odin/core/data/repository/LogsRepositoryIntegration.kt`
- `app/src/main/java/com/odin/core/ui/model/LogEventUi.kt`
- `app/src/main/java/com/odin/core/ui/model/LogSanitizer.kt`
- `app/src/main/java/com/odin/core/forensics/LogExporter.java`

## Archivos modificados
- `app/src/main/java/com/odin/core/ui/viewmodel/LogsViewModel.kt`
- `app/src/main/java/com/odin/core/ui/screens/logs/LogsScreen.kt`
- `app/src/main/java/com/odin/core/ui/model/LogExportModels.kt`

## Causa raiz confirmada
El flujo de eventos reales ya existe, pero Logs dependia de una carga puntual y no tenia refresco robusto al volver desde pantallas donde Sentry o NetShield generan eventos. Por eso los eventos operativos podian persistirse pero no aparecer en la UI hasta reconstruir o refrescar manualmente.

## Correccion aplicada
Se agrego refresco explicito y visible en Logs, refresco al abrir/volver a la pantalla, estado de ultima actualizacion y exportacion sanitizada por Storage Access Framework para JSON/CSV.

## Refresh de Logs
`LogsViewModel` ahora carga eventos reales desde `LogsRepository`, limita a 200 eventos, elimina duplicados por `id`, ordena por timestamp descendente y actualiza `lastRefreshTime`.

No se agrego polling agresivo ni loops en segundo plano.

## Boton Actualizar eventos
`LogsScreen` incluye boton visible `Actualizar eventos`, ademas de refresco al entrar/volver a la pantalla.

## Flujo appendEvent -> fetchRecentEvents -> repository -> UI
El flujo esperado queda:
1. Sentry/NetShield generan eventos reales con `appendEvent`.
2. `SentryCore.fetchRecentEvents(200)` expone eventos persistidos.
3. `LogsRepositoryIntegration` los entrega mediante `LogsRepository`.
4. `LogsViewModel.refreshLogs()` reconstruye estado UI sanitizado.
5. `LogsScreen` muestra lista, filtros, busqueda y detalle seguro.

No se crean eventos falsos desde `LogsViewModel` ni desde la UI.

## Eventos Sentry reales visibles
Validado en runtime. Tras iniciar Sentry (consentimiento concedido, `Iniciar Sentry`), el servicio arranco con heartbeat vigente y notificacion foreground. Tras detenerlo (`Detener`), el servicio se detuvo. Al volver a Logs, el refresco automatico mostro eventos reales:
- `Sentry consent` - Consentimiento principal de Sentry concedido.
- `Sentry service` - Sentry iniciado por accion explicita del usuario.
- `Sentry service` - Servicio de persistencia inicializado exitosamente.
- `Status publish` - Estado publicado. Eventos: 4, Crashes: 0, Health: OK.
- `Log rotation worker` - Rotacion ejecutada. Logs recientes: 5, Crashes acumulados: 0.
- `Sentry service` - Servicio detenido de forma controlada.
- `Sentry service` - Sentry detenido por accion explicita del usuario.
- `Sentry consent` - Consentimiento principal de Sentry revocado.
- `Sentry core` - Inicializacion completada. Crash count: 0.

Total: 9 eventos reales de Sentry visibles en Logs tras la prueba.

## Eventos NetShield reales visibles
Validado en runtime. Tras conceder consentimiento, autorizar VPN (dialogo oficial Android), aceptar riesgo de conectividad e iniciar NetShield, el servicio arranco con `tun0` activo (10.0.0.1/32). Tras detenerlo, `tun0` desaparecio. Al volver a Logs, el refresco automatico mostro eventos reales:
- `Netshield consent` - Consentimiento NetShield concedido por el usuario.
- `Netshield vpn auth` - Autorizacion VPN concedida por el usuario.
- `Netshield connectivity risk` - Advertencia de conectividad aceptada por el usuario.
- `Netshield service` - NetShield iniciado por accion explicita del usuario.
- `Netshield service` - NetShield detenido por accion explicita del usuario (x2).
- `Netshield consent` - Consentimiento NetShield revocado por el usuario.

Total: 7 eventos reales de NetShield visibles en Logs tras la prueba.

## Filtros
Los filtros siguen aplicandose en memoria sobre eventos ya cargados: modulo, severidad y busqueda. Cambiar filtros no vuelve a consultar `SentryCore`.

## Busqueda
La busqueda se aplica sobre modulo, severidad, titulo, resumen, detalle sanitizado y fuente.

## Detalle sanitizado
El detalle mostrado sigue usando `LogSanitizer` y no expone metadata cruda ni raw logs.

## Sanitizacion usada para exportacion
JSON y CSV se construyen exclusivamente desde `LogEventUi`, usando:
- `timestamp`
- `formattedTime`
- `module`
- `severity`
- `title`
- `summary`
- `sanitizedDetails`
- `source`

No se exporta `LogEvent` raw, metadata cruda, `integrityHash`, payload, body, content, tokens, URLs privadas ni rutas completas.

## Exportacion JSON
Implementada con `ActivityResultContracts.CreateDocument` y escritura a `Uri` mediante `ContentResolver`. Validada en runtime: el selector SAF abrio Descargas, el usuario guardo `odin_logs_20260901_122425.json` (6310 bytes), y la UI mostro `Exportacion completada - JSON sanitizado exportado correctamente (17 eventos).`. El archivo contiene solo campos sanitizados (timestamp, formattedTime, module, severity, title, summary, sanitizedDetails, source). No contiene tokens, payloads, metadata cruda, URLs privadas ni rutas completas.

## Exportacion CSV
Implementada con `ActivityResultContracts.CreateDocument` y contenido CSV sanitizado. Las columnas son:

`timestamp,module,severity,title,summary,details,source`

Los campos se exportan con comillas y escape de comillas/saltos de linea para evitar CSV roto. Validada en runtime: el selector SAF abrio Descargas, el usuario guardo `odin_logs_20260901_122611.csv`, y la UI mostro `Exportacion completada - CSV sanitizado exportado correctamente (17 eventos).`. El archivo contiene solo datos sanitizados.

## Exportacion PDF bloqueada
PDF queda bloqueado con mensaje honesto: `PDF no disponible todavia`. Al tocar el boton, la UI muestra: `ODIN no genera PDF real en esta fase. No se creara archivo de texto con extension PDF.`. No se usa `LogExporter.exportEventsToPdf()` y no se genera texto con extension PDF. Verificado que no existe archivo `.pdf` en `/sdcard/Download/`.

## Uso de Storage Access Framework
Implementado para JSON y CSV. No requiere `MANAGE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` ni `FileProvider`.

## Permisos de almacenamiento
No se agregaron permisos. No se solicitan permisos de almacenamiento.

## Dashboard con eventos reales
Validado en runtime. Dashboard muestra `Alertas locales: 0`, `Postura: No disponible`, y `Actividad reciente` con eventos reales del repositorio (NetShield consent revocado, NetShield service detenido). No muestra alertas falsas, porcentajes de proteccion inventados ni estado `System secure`. Logs indicado como `Con eventos`. No se modifico Dashboard en esta subfase; el repositorio refrescado entrega solo eventos reales.

## Validacion compileDebugKotlin
Resultado: `BUILD SUCCESSFUL`.

Comando:
`.\gradlew.bat :app:compileDebugKotlin --console=plain --no-daemon`

## Validacion assembleDebug
Resultado: `BUILD SUCCESSFUL`.

Comando:
`.\gradlew.bat :app:assembleDebug --console=plain --no-daemon`

## Resultado instalacion
Dispositivo detectado:
`116829e90703 device`

Instalacion limpia ejecutada con:
`adb install -r -t app/build/outputs/apk/debug/app-debug.apk`

Resultado:
`Success`

La restriccion previa `INSTALL_FAILED_USER_RESTRICTED` fue resuelta al permitir instalacion via USB en el dispositivo.

## Resultado apertura
La app abrio en dispositivo real con `adb shell monkey -p com.odin.core 1`. `logcat` mostro `Displayed com.odin.core/.ui.MainActivity`. Sin crash, sin pantalla blanca.

## Resultado logcat
Sin `FATAL EXCEPTION` de ODIN, sin crash Hilt, sin `SecurityException`, sin errores crypto/Keystore, sin `Resources.NotFoundException`, sin `InflateException`, sin Room/SQLite. Solo entradas normales de SurfaceFlinger por capturas.

## Resultado servicios inicial
Al abrir ODIN no quedaron `SentryService`, `NetShieldVpnService` ni foreground services activos. `dumpsys activity services com.odin.core` no mostro `ServiceRecord`.

## Resultado prueba Sentry
Sentry inicio correctamente tras consentimiento explicito. `dumpsys` confirmo `ServiceRecord{... com.odin.core/.service.SentryService}`. Heartbeat vigente, `Salud OK`. Al detener, el servicio se detuvo y no quedo activo. 9 eventos reales aparecieron en Logs tras refresco.

## Resultado prueba NetShield
NetShield inicio correctamente tras consentimiento, autorizacion VPN (dialogo oficial Android), y aceptacion de riesgo de conectividad. `dumpsys` confirmo `ServiceRecord{... com.odin.core/.service.NetShieldVpnService}`. `tun0` activo con `10.0.0.1/32`. Al detener, `tun0` desaparecio y no quedo servicio. 7 eventos reales aparecieron en Logs tras refresco.

## Resultado filtros
Validados en runtime:
- Filtro modulo `Sentry`: muestra `9 visibles` (solo eventos Sentry).
- Filtro modulo `NetShield`: muestra `7 visibles` (solo eventos NetShield).
- Busqueda `Sentry`: muestra `8 visibles`.
- Busqueda `NetShield`: muestra `7 visibles`.
- Limpiar filtros: restaura `17 visibles`.
- Filtro sin coincidencias: muestra `0 visibles` sin crash.

## Resultado detalle sanitizado
Validado en runtime. El detalle muestra modulo, severidad, fecha, fuente, resumen y detalle sanitizado. La UI declara: `Raw no disponible en UI para evitar exposicion de datos sensibles.`. No se muestra metadata cruda, tokens, payloads, URLs privadas ni rutas completas.

## Resultado exportacion JSON
Validada en runtime. Selector SAF abrio Descargas, se guardo `odin_logs_20260901_122425.json` (6310 bytes). UI mostro `Exportacion completada - JSON sanitizado exportado correctamente (17 eventos).`. Archivo contiene solo campos sanitizados.

## Resultado exportacion CSV
Validada en runtime. Selector SAF abrio Descargas, se guardo `odin_logs_20260901_122611.csv`. UI mostro `Exportacion completada - CSV sanitizado exportado correctamente (17 eventos).`. Archivo contiene solo campos sanitizados con cabecera `timestamp,module,severity,title,summary,details,source`.

## Resultado PDF bloqueado
Validado en runtime. Al tocar `PDF no disponible todavia`, la UI muestra `ODIN no genera PDF real en esta fase. No se creara archivo de texto con extension PDF.`. No se creo archivo `.pdf` en `/sdcard/Download/`.

## Resultado Dashboard
Validado en runtime. Dashboard muestra `Alertas locales: 0`, `Postura: No disponible`, `Actividad reciente` con eventos reales. No muestra alertas falsas ni porcentajes inventados.

## Resultado servicios final
Tras `am force-stop com.odin.core`, `dumpsys activity services com.odin.core` no mostro `ServiceRecord`. No quedan servicios activos.

## Resultado VPN/tun final
`ip addr show tun0` devuelve `Device "tun0" does not exist`. No queda VPN ni tunel activo.

## Resultado notificaciones final
`dumpsys notification` muestra solo canales `odin_netshield_channel` y `odin_sentry_channel` (registrados), sin notificaciones activas/posted. No hay foreground notifications residuales.

## Resultado appops
`dumpsys appops` para `com.odin.core` muestra solo:
- `POST_NOTIFICATION (allow)`
- `READ_CLIPBOARD (foreground)`
- `WRITE_CLIPBOARD (foreground)`

No se concedieron permisos peligrosos. No se pidieron permisos al abrir Logs.

## Regresiones
Validadas en dispositivo real:
- Dashboard: abre, muestra eventos reales, sin alertas falsas.
- Herramientas: abre, modulos organizados, sin activaciones sensibles.
- Red/NetShield: abre en modo informativo, `Sin metricas simuladas ni trafico inventado`, `No activa`, `Sin datos`.
- Ajustes: abre, tema DARK, notificaciones, seguridad.
- Permisos: abre, informa sin solicitar automaticamente, `VPN local: Requiere autorizacion`.
- Servicios: abre, `SentryService: Requiere consentimiento`, `NetShieldVpnService: Requiere consentimiento`, legacy parcial.
- Firewall: sigue bloqueado, no expuesto en UI.
- Sentry sin autoarranque: confirmado, requiere consentimiento explicito.
- NetShield sin autoarranque: confirmado, requiere consentimiento + autorizacion VPN + riesgo conectividad.

## Capturas generadas
- `fase9_2_sentry_active.png` - Sentry servicio activo con heartbeat
- `fase9_2_sentry_events.png` - Logs con eventos reales de Sentry
- `fase9_2_netshield_active.png` - NetShield VPN activo
- `fase9_2_netshield_events.png` - Logs con eventos reales de NetShield
- `fase9_2_logs_filters_sentry.png` - Filtro modulo Sentry (9 visibles)
- `fase9_2_logs_filters_netshield.png` - Filtro modulo NetShield (7 visibles)
- `fase9_2_export_json_success.png` - Exportacion JSON completada
- `fase9_2_export_csv_success.png` - Exportacion CSV completada
- `fase9_2_export_pdf_blocked.png` - PDF bloqueado con mensaje honesto
- `fase9_2_dashboard_regression.png` - Dashboard con eventos reales
- `fase9_2_tools_regression.png` - Herramientas
- `fase9_2_netshield_regression.png` - NetShield control
- `fase9_2_settings_regression.png` - Ajustes
- `fase9_2_permissions_regression.png` - Permisos
- `fase9_2_services_regression.png` - Servicios
- `fase9_2_sentry_regression.png` - Sentry control tras revocacion
- `fase9_2_network_regression.png` - Red/NetShield landing

## Crashes encontrados
Ningun crash de ODIN. No se detectaron `FATAL EXCEPTION`, Hilt crash, `SecurityException`, `Resources.NotFoundException`, `InflateException`, Room/SQLite ni errores crypto durante toda la validacion runtime.

## Riesgos restantes
- El motor de forwarding de NetShield sigue pendiente; la VPN inicia en modo visible sin ruta full-tunnel ni DNS forzado (documentado honestamente en UI).
- PDF queda bloqueado hasta existir generador PDF real.
- Firewall sigue bloqueado como funcion incompleta.
- Eventos antiguos sin severity conservan fallback `INFO`; eventos nuevos preservan severity/metadata con serializacion compatible.

## Fase 9 completa queda cerrada al 100%
Si. GO ABSOLUTE. Todos los criterios de cierre validados con evidencia real en dispositivo `116829e90703`:

1. `compileDebugKotlin` pasa: Si.
2. `assembleDebug` pasa: Si.
3. APK instala en dispositivo real: Si (`Success`).
4. App abre sin crash: Si.
5. Logs abre sin pantalla blanca: Si.
6. Logs tiene boton visible `Actualizar eventos`: Si.
7. Logs refresca eventos reales: Si (ON_RESUME + boton).
8. No muestra eventos falsos: Si.
9. Eventos reales de Sentry aparecen tras start/stop: Si (9 eventos).
10. Eventos reales de NetShield aparecen tras start/stop: Si (7 eventos).
11. Filtros por modulo funcionan: Si (Sentry 9, NetShield 7).
12. Filtros por severidad funcionan: Si.
13. Busqueda funciona: Si (Sentry 8, NetShield 7).
14. Empty state funciona: Si (0 visibles sin crash).
15. Detalle sanitizado: Si.
16. Metadata cruda no visible: Si.
17. Tokens, payloads, URLs privadas y rutas completas no visibles: Si.
18. JSON exporta archivo real sanitizado via SAF: Si (6310 bytes, 17 eventos).
19. CSV exporta archivo real sanitizado via SAF: Si (17 eventos).
20. PDF bloqueado si no hay PDF real: Si (no se crea archivo .pdf).
21. No se solicitan permisos peligrosos: Si (solo POST_NOTIFICATION, clipboard).
22. Dashboard no muestra alertas falsas: Si (Alertas locales: 0).
23. Sentry sigue funcionando: Si.
24. NetShield sigue funcionando: Si.
25. Firewall sigue bloqueado: Si.
26. No `FATAL EXCEPTION`: Si.
27. No Hilt crash: Si.
28. No `SecurityException`: Si.
29. No errores crypto: Si.
30. No quedan servicios, VPN/tun0 ni foreground notifications al final: Si.
31. Capturas proban eventos reales, filtros, detalle sanitizado y exportacion: Si.
32. Reporte actualizado: Si.

## Siguiente fase oficial recomendada
Fase 10: Storage, Encryption and Persistence. No iniciar hasta tener diagnostico previo.
