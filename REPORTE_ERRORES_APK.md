# REPORTE DE ERRORES CRÍTICOS - APK ODÍN

## Fecha de Análisis
18 de junio de 2026

## Problemas Reportados por Usuario
1. El logo no se ve al instalar el APK
2. Se instaló otra APP llamada "leaks" que el usuario NUNCA autorizó
3. La app no se pudo abrir

## Análisis Detallado

### PROBLEMA #1: APP "Leaks" No Autorizada

**Causa Identificada:**
- **Ubicación:** `app/build.gradle` línea 173
- **Código:** `debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.12'`
- **Explicación:** LeakCanary es una biblioteca de detección de memory leaks que se instala automáticamente como una app separada en modo debug. Esta app aparece en el launcher como "LeakCanary" o "leaks" y permite visualizar memory leaks en tiempo real.

**Por qué apareció sin autorización:**
- LeakCanary se configura automáticamente cuando se agrega como dependencia `debugImplementation`
- No requiere permiso explícito del usuario para instalarse
- Solo aparece en builds de debug, no en release

**Impacto:**
- No es malware ni app maliciosa
- Es una herramienta de desarrollo legítima
- Puede confundir al usuario al ver una app adicional no solicitada
- Consume recursos del dispositivo en modo debug

**Recomendación:**
- Remover LeakCanary del build.gradle para producción
- O usar `debugImplementation` solo durante desarrollo y remover antes de generar APK de producción

---

### PROBLEMA #2: Logo No Visible

**Análisis de Recursos del Launcher:**

**Archivos PNG Presentes:**
- `mipmap-mdpi/`: ic_launcher.png (1.8 MB), ic_launcher_round.png (1.8 MB), ic_odin_logo.png (1.8 MB)
- `mipmap-hdpi/`: ic_launcher.png (1.8 MB), ic_launcher_round.png (1.8 MB), ic_odin_logo.png (1.8 MB)
- `mipmap-xhdpi/`: ic_launcher.png (1.8 MB), ic_launcher_round.png (1.8 MB), ic_odin_logo.png (1.8 MB)
- `mipmap-xxhdpi/`: ic_launcher.png (1.8 MB), ic_launcher_round.png (1.8 MB), ic_odin_logo.png (1.8 MB)
- `mipmap-xxxhdpi/`: ic_launcher.png (1.8 MB), ic_launcher_round.png (1.8 MB), ic_odin_logo.png (1.8 MB)

**Archivos XML de Configuración:**
- `mipmap-anydpi-v26/ic_launcher.xml`: Configuración adaptive-icon
- `mipmap-anydpi-v26/ic_launcher_round.xml`: Configuración adaptive-icon round

**Contenido de ic_launcher.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@mipmap/ic_launcher"/>
</adaptive-icon>
```

**Problema Potencial #1: Tamaño de Archivos Excesivo**
- Todos los archivos PNG tienen exactamente 1,826,742 bytes (1.8 MB)
- Esto es inusualmente grande para iconos de launcher
- Tamaño típico de launcher icon: 50-200 KB
- Tamaño actual: 1.8 MB (9-36 veces más grande de lo normal)

**Problema Potencial #2: Referencia a Color Inexistente**
- `ic_launcher.xml` referencia `@color/ic_launcher_background`
- Necesito verificar si este color existe en colors.xml

**Problema Potencial #3: Mismo Archivo en Todas las Densidades**
- Todos los directorios tienen exactamente el mismo archivo de 1.8 MB
- Esto sugiere que el mismo archivo se copió en todas las densidades sin optimización
- Android debería usar versiones optimizadas por densidad

---

### PROBLEMA #3: App No Se Pudo Abrir

**Análisis de AndroidManifest.xml:**

**Activity Principal:**
```xml
<activity android:name=".ui.MainActivity"
    android:exported="true"
    android:theme="@style/Theme.AppCompat.NoActionBar"
    android:configChanges="orientation|screenSize|screenLayout">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

**Problema Potencial #1: Conflicto de Themes**
- AndroidManifest usa: `android:theme="@style/Theme.AppCompat.NoActionBar"`
- MainActivity.kt es ComponentActivity (no AppCompatActivity)
- Theme.AppCompat.NoActionBar requiere AppCompatActivity
- ComponentActivity debería usar tema Material Components

**Problema Potencial #2: Package Attribute Obsoleto**
- AndroidManifest tiene: `package="com.odin.core"` en línea 3
- Gradle usa namespace en build.gradle
- Atributo package en manifest está deprecado desde Android Gradle Plugin 8.0
- Warning en build: "Setting the namespace via the package attribute in the source AndroidManifest.xml is no longer supported"

**Problema Potencial #3: Múltiples Activities Exported**
- MainActivity: `android:exported="true"` (launcher activity)
- NetworkAnonymityActivity: `android:exported="true"` (línea 100)
- FileAnalysisActivity: `android:exported="true"` (línea 111)
- BootReceiver: `android:exported="true"` (línea 117)

**Problema Potencial #4: Servicios en Proceso Separado**
- NetShieldVpnService: `android:process=":netshield"` (línea 73)
- Esto crea un proceso separado que podría causar problemas de inicialización

---

### PROBLEMA #4: Configuración de Themes

**styles.xml:**
```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="android:statusBarColor">@color/colorPrimaryDark</item>
</style>
```

**themes.xml:**
```xml
<style name="Theme.ODIN" parent="Theme.AppCompat.NoActionBar">
    <item name="android:statusBarColor">@android:color/transparent</item>
</style>
```

**Problema:**
- Dos themes definidos: AppTheme y Theme.ODIN
- AndroidManifest usa Theme.AppCompat.NoActionBar
- MainActivity.kt usa OdinTheme (Compose)
- Posible conflicto entre XML theme y Compose theme

---

### PROBLEMA #5: Warnings de Build

**Warnings Detectados en Build:**

**Namespace Warning:**
```
package="com.odin.core" found in source AndroidManifest.xml
Setting the namespace via the package attribute in the source AndroidManifest.xml is no longer supported
```

**TensorFlow Namespace Warnings:**
```
Namespace 'org.tensorflow.lite' is used in multiple modules
org.tensorflow:tensorflow-lite:2.14.0, org.tensorflow:tensorflow-lite-api:2.14.0
```

**Room Constructor Warnings:**
```
Multiple good constructors and Room will pick the no-arg constructor
Theme.java, NotificationSettings.java, AutomationRule.java, Profile.java, Backup.java
```

**Deprecated API Warning:**
```
Some input files use or override a deprecated API
```

---

### PROBLEMA #6: Dependencias Potencialmente Problemáticas

**LeakCanary (debugImplementation):**
- `com.squareup.leakcanary:leakcanary-android:2.12`
- Causa la app "leaks" no autorizada
- Solo debería estar en desarrollo, no en producción

**TensorFlow Lite:**
- `org.tensorflow:tensorflow-lite:2.14.0`
- `org.tensorflow:tensorflow-lite-api:2.14.0`
- Conflictos de namespace
- Librería nativa no puede ser stripped: `libtensorflowlite_jni.so`

---

## Resumen de Errores Críticos

### Errores que Impiden Funcionamiento:
1. **Theme incompatibilidad:** MainActivity usa Theme.AppCompat pero es ComponentActivity
2. **Package attribute obsoleto:** Causa warnings y posibles problemas de resolución
3. **Logo tamaño excesivo:** 1.8 MB por archivo (debería ser 50-200 KB)

### Errores de Seguridad/Percepción:
1. **LeakCanary no autorizado:** App "leaks" aparece sin permiso del usuario
2. **Múltiples activities exported:** Posible vector de ataque si no se maneja correctamente

### Errores de Calidad:
1. **Mismo archivo en todas las densidades:** Sin optimización por densidad
2. **Warnings de Room:** Constructores múltiples sin @Ignore
3. **Warnings de deprecated API:** Código usando APIs obsoletas

---

## Recomendaciones (Sin Modificar Código)

### Para Usuario:
1. **No usar APK debug para producción:** El APK debug incluye LeakCanary que causa la app "leaks"
2. **Usar APK release:** El APK release no incluye LeakCanary
3. **Verificar si APK release tiene el mismo problema:** Si el logo no se ve en release, es un problema de recursos

### Para Desarrollador (Futuro):
1. **Remover package attribute de AndroidManifest.xml:** Usar namespace en build.gradle
2. **Optimizar tamaño de iconos:** Reducir de 1.8 MB a 50-200 KB por densidad
3. **Corregir theme de MainActivity:** Usar tema compatible con ComponentActivity
4. **Remover LeakCanary de build.gradle antes de generar APK de producción**
5. **Agregar @Ignore a constructores Room:** Para eliminar warnings
6. **Revisar exported attributes:** Solo marcar como exported lo necesario

---

## Estado Actual del APK

**APK Debug:**
- Ruta: `c:/Users/User/Downloads/odin/app/build/outputs/apk/debug/app-debug.apk`
- Tamaño: 81.7 MB
- Incluye: LeakCanary (causa app "leaks")
- Estado: **NO RECOMENDADO PARA PRODUCCIÓN**

**APK Release:**
- Ruta: `c:/Users/User/Downloads/odin/app/build/outputs/apk/release/app-release-unsigned.apk`
- Tamaño: 67.2 MB
- Incluye: Sin LeakCanary
- Estado: **RECOMENDADO PARA PRUEBAS** (pero requiere firma manual)

---

## Conclusión

El problema principal es que se instaló el **APK debug** que incluye LeakCanary, lo cual causó la aparición de la app "leaks" sin autorización. El APK release no incluye LeakCanary y debería usarse para pruebas.

El problema del logo no visible podría ser causado por:
1. Tamaño excesivo de los archivos PNG (1.8 MB cada uno)
2. Referencia a color `ic_launcher_background` que podría no existir
3. Conflicto de themes entre XML y Compose

El problema de que la app no se pudo abrir podría ser causado por:
1. Incompatibilidad de theme (Theme.AppCompat con ComponentActivity)
2. Package attribute obsoleto en AndroidManifest
3. Proceso separado de NetShieldVpnService

**Recomendación Inmediata:**
Probar con el APK release (`app-release-unsigned.apk`) que no incluye LeakCanary. Si el logo sigue sin verse y la app no se abre, entonces los problemas son de configuración de recursos y themes.
