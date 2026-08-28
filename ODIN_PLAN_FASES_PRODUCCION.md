# ODIN - Plan de Fases para Produccion

Este documento define una ruta de trabajo ordenada para estabilizar, auditar, redisenar y preparar ODIN para produccion sin asumir funcionalidades no verificadas.

La regla principal es: **si no esta confirmado en archivos reales del proyecto o en una prueba ejecutada, no se afirma como funcional**.

## Protocolo Anti-Alucinacion

Antes de reportar una funcionalidad, riesgo o avance:

1. Citar el archivo real revisado.
2. Indicar si se verifico por lectura, build, test, instalacion o ejecucion en dispositivo.
3. Clasificar el estado con una de estas etiquetas:
   - Confirmado
   - Parcial
   - Roto
   - Simulado
   - Planeado
   - Inexistente
4. No usar textos de comentarios como prueba suficiente. Si el comentario dice "REAL", debe comprobarse contra codigo ejecutable.
5. No marcar una fase como completada si no se ejecuto su validacion minima.
6. No introducir nuevas dependencias sin justificar necesidad, impacto, compatibilidad y riesgo.
7. No modificar permisos, servicios, VPN, cifrado o almacenamiento sin explicar impacto en privacidad y Google Play.
8. No cambiar arquitectura completa sin recuperar primero compilacion estable.
9. No eliminar archivos sin justificar uso actual, reemplazo y riesgo.
10. Todo cambio debe quedar asociado a una fase, archivos tocados, motivo y validacion.

## Estado Inicial Confirmado

Ultima auditoria inicial:

- Proyecto Android Application.
- Package/applicationId: `com.odin.core`.
- Gradle wrapper: `8.2`.
- Android Gradle Plugin efectivo: `8.2.2`.
- `compileSdk`: 34.
- `minSdk`: 26.
- `targetSdk`: 34.
- Configuracion Gradle efectiva: `settings.gradle` y `build.gradle`.
- Modulo efectivo actual: `:app`.
- `:benchmark` existe en disco, pero solo esta incluido en `settings.gradle.kts`, no en `settings.gradle`.
- El proyecto no compila actualmente: falla `:app:compileDebugKotlin`.

## Criterios Globales de Calidad

ODIN no puede considerarse listo para produccion hasta cumplir:

1. Compila `debug` sin errores.
2. Compila `release` sin errores.
3. Instala en dispositivo real.
4. Abre sin crash.
5. No muestra icono Android por defecto.
6. No usa datos simulados como si fueran datos reales.
7. Los permisos son progresivos, explicados y revocables.
8. SentryCore es visible, controlable y no oculto.
9. NetShield/VPN es visible, controlable y no invasivo.
10. Logs no exponen datos sensibles innecesarios.
11. Notificaciones foreground son profesionales y con canales correctos.
12. No hay cifrado inseguro conocido en rutas productivas.
13. No hay migraciones destructivas silenciosas.
14. La UI principal es profesional, responsiva y centrada en el usuario.
15. Google Play policy review no tiene riesgos obvios sin mitigacion documentada.

## Fase 0 - Recuperar Build Verde y Auditoria Base

Objetivo:
Recuperar una base compilable antes de redisenar o endurecer.

Archivos iniciales a revisar:

- `app/src/main/java/com/odin/core/customization/ui/SettingsActivity.kt`
- `app/src/main/java/com/odin/core/data/repository/FirewallRepositoryImpl.kt`
- `app/src/main/java/com/odin/core/data/repository/LogsRepositoryImpl.kt`
- `app/src/main/java/com/odin/core/data/repository/LogsRepositoryIntegration.kt`
- `app/src/main/java/com/odin/core/data/repository/QuarantineRepositoryImpl.kt`
- `app/src/main/java/com/odin/core/data/repository/ThreatsRepositoryImpl.kt`
- `app/src/main/java/com/odin/core/di/IntegrationModule.kt`
- `app/src/main/java/com/odin/core/forensics/LogExporter.java`

Tareas:

1. Reproducir el error con `./gradlew.bat :app:compileDebugKotlin --console=plain`.
2. Corregir solo errores de compilacion.
3. No redisenar UI todavia.
4. No cambiar comportamiento de servicios salvo si impide compilar.
5. Documentar cada contrato corregido.
6. Ejecutar build debug.

Riesgos:

- Cambios pequenos pueden afectar Hilt, repositorios o tipos compartidos.
- Corregir por "stub" o datos falsos queda prohibido.

Criterio de finalizacion:

- `:app:compileDebugKotlin` pasa.
- `:app:assembleDebug` pasa.
- Lista de archivos modificados documentada.

## Fase 1 - Inventario Funcional Verificado

Objetivo:
Crear mapa real de funcionalidades existentes y no existentes.

Tareas:

1. Revisar `AndroidManifest.xml`.
2. Revisar servicios: SentryCore, SentryService, NetShieldVpnService, VPN anonymity, Tor service si aplica.
3. Revisar paquetes `network`, `persistence`, `forensics`, `fileanalysis`, `protection`, `data`, `domain`, `ui`.
4. Clasificar cada funcionalidad:
   - Implementada y estable
   - Implementada pero riesgosa
   - Parcialmente implementada
   - Rota
   - Simulada
   - Inexistente
5. Marcar funciones que no deben mostrarse como activas en UI.

Criterio de finalizacion:

- Tabla de funcionalidades con archivo fuente asociado.
- Ninguna feature aparece como "funcional" sin evidencia.

## Fase 2 - Cumplimiento, Permisos y Consentimiento

Objetivo:
Evitar comportamiento interpretable como spyware, stalkerware o malware.

Archivos clave:

- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/odin/core/ui/MainActivity.kt`
- `app/src/main/java/com/odin/core/receiver/BootReceiver.java`
- `app/src/main/java/com/odin/core/service/SentryService.java`
- `app/src/main/java/com/odin/core/service/NetShieldVpnService.java`

Tareas:

1. Auditar permisos declarados.
2. Justificar o marcar para eliminacion permisos sensibles:
   - `SYSTEM_ALERT_WINDOW`
   - `MANAGE_EXTERNAL_STORAGE`
   - `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`
   - `USE_FULL_SCREEN_INTENT`
   - `FOREGROUND_SERVICE_SPECIAL_USE`
   - `BIND_VPN_SERVICE`
   - `RECEIVE_BOOT_COMPLETED`
3. Definir consentimiento progresivo.
4. Evitar pedir todos los permisos en `onCreate`.
5. Crear matriz permiso -> funcion -> justificacion -> estado.
6. Definir controles para activar/desactivar servicios.

Criterio de finalizacion:

- Cada permiso tiene justificacion tecnica y UX.
- Funciones sensibles no se activan sin accion clara del usuario.

## Fase 3 - Identidad Visual e Icono ODIN

Objetivo:
Eliminar icono Android por defecto e integrar el logo real del ojo de ODIN.

Archivos clave:

- `app/src/main/res/mipmap-mdpi/`
- `app/src/main/res/mipmap-hdpi/`
- `app/src/main/res/mipmap-xhdpi/`
- `app/src/main/res/mipmap-xxhdpi/`
- `app/src/main/res/mipmap-xxxhdpi/`
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`
- `app/src/main/AndroidManifest.xml`
- servicios con `setSmallIcon(...)`

Tareas:

1. Confirmar asset fuente del logo.
2. Generar densidades correctas sin deformacion.
3. Configurar adaptive icon.
4. Configurar round icon.
5. Declarar `android:icon` y `android:roundIcon` en Manifest.
6. Crear icono monochrome si aplica.
7. Revisar iconos de notificacion foreground.
8. Validar visualmente APK instalada.

Criterio de finalizacion:

- Launcher no muestra icono Android por defecto.
- Notificaciones no usan iconos genicos de Android.
- Icono se ve bien en launcher, recents y ajustes del sistema.

## Fase 4 - Sistema Visual Base

Objetivo:
Crear una identidad UI dark, premium, consistente y mantenible.

Archivos clave:

- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/themes.xml`
- `app/src/main/res/values/styles.xml`
- `app/src/main/res/values/strings.xml`
- crear si falta: `dimens.xml`
- drawables reutilizables para tarjetas, chips, botones y fondos.

Tareas:

1. Definir colores centralizados:
   - fondo principal oscuro
   - tarjetas oscuro-azuladas
   - borde sutil
   - verde ODIN
   - naranja advertencia
   - rojo error
   - texto principal/secundario
2. Corregir encoding de strings visibles.
3. Sacar textos hardcodeados de XML/Java cuando sea razonable.
4. Crear estilos para botones, cards, chips y estados.
5. No introducir dependencia visual nueva si puede hacerse con Material/AppCompat/Compose existente.

Criterio de finalizacion:

- Pantallas existentes pueden consumir recursos visuales centralizados.
- No hay paleta prototipo blanca en pantallas objetivo.

## Fase 5 - Dashboard Profesional

Objetivo:
Convertir Inicio en centro de control real y transparente.

Pantalla debe mostrar solo datos reales o estados "pendiente/no disponible".

Elementos:

- Logo ODIN.
- Estado general:
  - Protegido
  - Proteccion parcial
  - Requiere atencion
  - Inactivo
- Porcentaje calculado desde estados reales.
- Estado SentryCore.
- Estado NetShield.
- Estado VPN local.
- Amenazas bloqueadas ultimas 24h si existe dato real.
- Escaneos recientes si existe dato real.
- Ultima actividad.
- Accesos rapidos.
- Navegacion inferior.

Tareas:

1. Localizar fuente real de estado de SentryCore.
2. Localizar fuente real de estado de NetShield/VPN.
3. Definir modelo UI de dashboard.
4. Mostrar "Sin datos todavia" cuando no exista fuente real.
5. Evitar graficas falsas.

Criterio de finalizacion:

- Dashboard abre sin crash.
- No usa numeros falsos.
- Usuario entiende estado real del dispositivo.

## Fase 6 - Navegacion y Pantallas Principales

Objetivo:
Ordenar ODIN como producto usable.

Pantallas objetivo:

1. Inicio / Dashboard.
2. Herramientas.
3. Panel de control.
4. Red / NetShield.
5. Logs / Reportes.
6. Ajustes.
7. Permisos.
8. Detalle de eventos.
9. Estado de servicios.
10. Acerca de ODIN.

Tareas:

1. Revisar navegacion actual Compose.
2. Decidir si se mantiene Compose para main UI.
3. No migrar XML legacy salvo que sea necesario.
4. Crear pantalla Herramientas con categorias:
   - Proteccion
   - Privacidad
   - Sistema
   - Red
5. Marcar herramientas no implementadas como "Planeado" o no mostrarlas.

Criterio de finalizacion:

- Navegacion inferior profesional.
- No hay pantalla principal de logs crudos.
- Herramientas no fingen funcionalidades inexistentes.

## Fase 7 - SentryCore y Foreground Service UI

Objetivo:
Hacer SentryCore visible, controlable y estable.

Archivos clave:

- `SentryCore.java`
- `SentryService.java`
- `BootReceiver.java`
- repositorios de logs/estado
- UI dashboard/control.

Tareas:

1. Verificar inicio/detencion real.
2. Evitar reinicios infinitos.
3. Mostrar estado en UI.
4. Profesionalizar notificacion.
5. Controlar permisos faltantes.
6. Revisar consumo de bateria.
7. Revisar `START_STICKY`.

Criterio de finalizacion:

- Usuario puede iniciar/detener.
- Servicio no queda oculto.
- Notificacion comunica claramente que ODIN esta activo.

## Fase 8 - NetShield, VPN Local y Red

Objetivo:
Hacer NetShield transparente, etico y verificable.

Archivos clave:

- `NetShieldVpnService.java`
- `PacketAuditor.java`
- `LocalFirewallEngine.java`
- `ThreatDetectionEngine.java`
- `DynamicRuleEngine.java`
- pantallas de red.

Tareas:

1. Confirmar flujo real de autorizacion VPN.
2. No activar VPN sin consentimiento.
3. No prometer descifrado TLS.
4. No recolectar contenido privado.
5. Mostrar metricas reales si existen.
6. Sustituir broadcast implicito por mecanismo interno seguro si procede.
7. Revisar DNS hardcodeado.

Criterio de finalizacion:

- VPN se inicia solo con autorizacion.
- UI muestra estado real.
- No hay datos de red sensibles expuestos innecesariamente.

## Fase 9 - Logs y Reportes

Objetivo:
Reemplazar logs crudos por eventos estructurados.

Tareas:

1. Revisar `LogEvent`, repositorios y exporters.
2. Crear lista con severidad, modulo, timestamp y resumen.
3. Crear detalle expandible.
4. Filtros por modulo/severidad.
5. Enmascarar IPs, rutas, dominios o tokens cuando sea necesario.
6. Exportacion segura solo si esta implementada y validada.
7. Corregir accesos privados/publicos de `LogExporter`.

Criterio de finalizacion:

- Logs utiles sin saturar UI.
- No se muestran rutas/PII innecesarias.
- Exportacion falla de forma segura si no esta disponible.

## Fase 10 - Almacenamiento, Cifrado y Persistencia

Objetivo:
Eliminar riesgos criptograficos y de perdida de datos.

Archivos clave:

- `BackupEncryptor.java`
- `SecureLocalStorage.java`
- `EnhancedSecureLocalStorage.java`
- `CustomizationDatabase.java`
- repositorios que usan `SharedPreferences`.

Tareas:

1. Reemplazar AES/ECB y clave hardcodeada.
2. Nunca devolver datos en claro si falla cifrado.
3. Revisar SharedPreferences no cifrado.
4. Revisar migraciones Room.
5. Activar `exportSchema` y migraciones reales cuando aplique.
6. Documentar datos sensibles y retencion.

Criterio de finalizacion:

- No quedan rutas productivas con cifrado debil conocido.
- Migraciones no destruyen datos silenciosamente.

## Fase 11 - File Analysis y Herramientas

Objetivo:
Mostrar escaner y analisis solo con capacidades reales.

Tareas:

1. Revisar scanner, malware, quarantine y reportes.
2. Verificar permisos de archivos.
3. Evitar `MANAGE_EXTERNAL_STORAGE` si SAF o permisos granulares bastan.
4. Redisenar UI de file analysis.
5. Confirmaciones antes de cuarentena/eliminacion.
6. Logs de archivos sin rutas sensibles completas.

Criterio de finalizacion:

- Analisis funciona con archivo seleccionado.
- Acciones destructivas piden confirmacion.
- Permisos se solicitan de forma minima.

## Fase 12 - Hardening Android y Google Play

Objetivo:
Reducir riesgo de rechazo, abuso o interpretacion maliciosa.

Checklist:

- Componentes exportados justificados.
- Receivers limitados.
- PendingIntents inmutables cuando aplica.
- Foreground service type correcto.
- Privacy disclosure para VPN/servicios/logs.
- Permisos peligrosos justificados.
- No accesibilidad abusiva.
- No all-files access sin necesidad central.
- Network security config revisada.
- Cleartext deshabilitado confirmado.
- Logs sensibles sanitizados.

Criterio de finalizacion:

- Matriz de cumplimiento lista.
- Riesgos restantes documentados.

## Fase 13 - Pruebas

Objetivo:
Validar en build, instalacion y dispositivo real.

Pruebas minimas:

1. `:app:assembleDebug`.
2. `:app:assembleRelease`.
3. Instalacion limpia.
4. Reinstalacion.
5. Arranque sin permisos.
6. Permisos concedidos.
7. Permisos revocados.
8. SentryCore iniciar/detener.
9. NetShield iniciar/detener.
10. VPN autorizada.
11. VPN denegada.
12. Notificaciones Android 13+.
13. Android 10, 12, 13, 14 y 15 si hay dispositivos/emuladores disponibles.
14. Rotacion si aplica.
15. Pantalla pequena y grande.
16. Logs largos.
17. Bateria basica.

Criterio de finalizacion:

- Matriz de pruebas con resultado, dispositivo/API y fecha.

## Fase 14 - Release y Publicacion

Objetivo:
Preparar APK/AAB productivo.

Tareas:

1. Revisar `versionCode` y `versionName`.
2. Revisar `minifyEnabled` y reglas ProGuard/R8.
3. Generar release firmado si hay keystore autorizado.
4. Verificar iconos.
5. Verificar strings visibles.
6. Verificar politica de privacidad.
7. Preparar notas de release.
8. Preparar justificacion de permisos para Play Console.
9. Preparar formulario VPN si aplica.

Criterio de finalizacion:

- AAB/APK release generado.
- Checklist de publicacion completo.

## Formato Obligatorio de Cierre por Fase

Al terminar cada fase, reportar:

```text
Fase:
Estado:
Archivos modificados:
Cambios realizados:
Por que se cambiaron:
Validaciones ejecutadas:
Resultado de validaciones:
Riesgos restantes:
Siguiente fase recomendada:
```

## Matriz de Funcionalidades

Usar esta tabla durante la auditoria funcional:

| Funcionalidad | Estado | Evidencia | Validacion | Riesgo | Decision UI |
|---|---|---|---|---|---|
| SentryCore | Pendiente de verificar | - | - | - | - |
| SentryService | Pendiente de verificar | - | - | - | - |
| NetShield VPN | Pendiente de verificar | - | - | - | - |
| Logs | Pendiente de verificar | - | - | - | - |
| Reportes | Pendiente de verificar | - | - | - | - |
| File scanner | Pendiente de verificar | - | - | - | - |
| Quarantine | Pendiente de verificar | - | - | - | - |
| DNS privacy | Pendiente de verificar | - | - | - | - |
| DNSSEC | Simulado detectado | `DNSSECManager.java` | Lectura | Alto | Mostrar como planeado/incompleto |
| DoH/DoT | Simulado detectado | `DoHManager.java`, `DoTManager.java` | Lectura | Alto | Mostrar como planeado/incompleto |
| IP leak detection | Parcial/simulado | `IPLeakDetector.java` | Lectura | Alto | No prometer proteccion completa |

## Orden Inmediato Recomendado

1. Fase 0: recuperar build verde.
2. Fase 1: inventario funcional con evidencia.
3. Fase 3: integrar icono/logo.
4. Fase 4: sistema visual base.
5. Fase 5: dashboard profesional con estados reales.

No avanzar a redisenos grandes hasta que Fase 0 este completada.
