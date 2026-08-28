# 🚀 ODIN FASE 1: GUÍA DE COMPILACIÓN Y DEPLOYMENT

**Documento:** SentryCore Build & Deployment Guide
**Fase:** 1 - Persistencia
**Fecha:** June 10, 2026

---

## 📋 PRE-REQUISITOS

### 1. Entorno
- **Android Studio**: 2024.1 o superior
- **Gradle**: 8.2 (incluido en proyecto)
- **Java**: 17+
- **Android SDK**: API 34 (compileSdk)
- **Mínimo**: API 26 (minSdk)

### 2. Dependencias Verificadas
```gradle
// En app/build.gradle:
- androidx.appcompat:appcompat:1.7.0 ✅
- androidx.core:core:1.12.0 ✅
- androidx.security:security-crypto:1.1.0-alpha06 ✅
- androidx.work:work-runtime:2.8.1 ✅
- androidx.room:room-runtime:2.6.1 ✅
- com.google.android.material:material:1.10.0 ✅
```

---

## 🔨 COMPILACIÓN

### Paso 1: Sincronizar Gradle
```bash
cd c:\Users\User\Downloads\odin
./gradlew sync
# Esperar a que Gradle descargue dependencias (~2-3 min)
```

### Paso 2: Verificar Errores de Compilación
```bash
./gradlew compileDebugSources
# Debe completarse sin errores
```

### Paso 3: Build Debug
```bash
./gradlew assembleDebug
# Genera APK en: app/build/outputs/apk/debug/
```

### Paso 4: Build Release (Obfuscado)
```bash
./gradlew assembleRelease
# Importante: Genera APK obfuscado con ProGuard
# Genera APK en: app/build/outputs/apk/release/
```

---

## ✅ VERIFICACIÓN POST-COMPILACIÓN

### 1. Verificar ProGuard se Ejecutó
```bash
# Abrir app/build/outputs/mapping/release/mapping.txt
# Debe contener mappings de clases obfuscadas

# Verificar que clases críticas NO fueron ofuscadas:
grep -i "sentrycore" mapping.txt
# Debe mostrar: com.odin.core.persistence.SentryCore -> Lcom/odin/core/persistence/SentryCore;
```

### 2. Verificar Dependencias se Incluyeron
```bash
# Descomprimir APK release
unzip app/build/outputs/apk/release/app-release.apk
cd META-INF

# Verificar que EncryptedSharedPreferences está presente
grep -r "androidx.security" . | wc -l
# Debe retornar > 0
```

### 3. Verificar Tamaño del APK
```bash
ls -lh app/build/outputs/apk/release/app-release.apk
# Tamaño esperado: 2-3 MB

# Breakdown (aproximado):
# - Core ODIN: ~500 KB
# - AndroidX: ~1.5 MB
# - Material Design: ~800 KB
```

---

## 🧪 TESTING PRE-DEPLOYMENT

### 1. Unit Tests (Criptografía)
```java
// En test/java/com/odin/core/security/CryptographicManagerTest.java
@Test
public void testAES256Encryption() {
    CryptographicManager crypto = new CryptographicManager(context);
    String plaintext = "Datos sensibles del ODIN";
    
    String encrypted = crypto.encrypt(plaintext);
    String decrypted = crypto.decrypt(encrypted);
    
    assertEquals(plaintext, decrypted);
    assertNotEquals(plaintext, encrypted);
}
```

### 2. Integration Tests (SentryCore)
```java
@Test
public void testSentriCoreThreadSafety() throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    SentryCore core = SentryCore.getInstance(context);
    
    for (int i = 0; i < 100; i++) {
        executor.submit(() -> {
            core.appendEvent(new LogEvent(
                System.currentTimeMillis(),
                "TEST",
                "Event " + Thread.currentThread().getId()
            ));
        });
    }
    
    executor.awaitTermination(10, TimeUnit.SECONDS);
    
    List<LogEvent> events = core.fetchRecentEvents(100);
    assertEquals(100, events.size()); // Sin race conditions
}
```

### 3. Instrumentation Tests (Device)
```bash
# Compilar tests
./gradlew assembleAndroidTest

# Ejecutar en emulador/dispositivo
./gradlew connectedAndroidTest

# Verificar que todas las pruebas pasan:
# ✓ CryptographicManagerTest
# ✓ SentriCoreTest
# ✓ GlobalExceptionHandlerTest
# ✓ EnhancedSecureLocalStorageTest
```

---

## 📱 INSTALACIÓN EN DISPOSITIVO

### 1. Conectar Dispositivo
```bash
# Conectar vía USB con debugging habilitado
adb devices
# Debe mostrar el dispositivo como "device"
```

### 2. Instalar APK Debug
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
# Esperar: "Success"

# Verificar instalación
adb shell pm list packages | grep odin
# Debe mostrar: package:com.odin.core
```

### 3. Verificar Permisos
```bash
# Los permisos críticos están en AndroidManifest.xml:
# - POST_NOTIFICATIONS ✓
# - FOREGROUND_SERVICE ✓
# - BIND_VPN_SERVICE ✓
# - REQUEST_IGNORE_BATTERY_OPTIMIZATIONS ✓

# Ver permisos instalados
adb shell pm list permissions -d | grep odin
```

### 4. Iniciar App y Verificar Logs
```bash
# Iniciar MainActivity
adb shell am start -n com.odin.core/com.odin.core.ui.MainActivity

# Ver logs en tiempo real
adb logcat | grep -E "SentryService|SentryCore|CryptographicManager"

# Debe mostrar:
# I/SentryService: SentryService inicializado exitosamente
# I/SentryCore: Inicialización completada...
```

---

## 🔍 VALIDACIÓN DE SEGURIDAD

### 1. Verificar Que NO Hay Datos en Plano
```bash
# Acceder al almacenamiento de la app
adb shell
su
cd /data/data/com.odin.core/shared_prefs/

# Ver archivo de preferencias
cat odin_encrypted_prefs.xml
# Debe mostrar: EncryptedSharedPreferences (ilegible)
# NO debe mostrar: datos en texto plano

exit
```

### 2. Verificar que Cifrado Funciona
```bash
# Ejecutar en Android Studio:
# 1. Poner breakpoint en CryptographicManager.encrypt()
# 2. Iniciar app en debug
# 3. Inspecionar:
#    - plaintext: legible
#    - ciphertext: ilegible (Base64)
#    - IV: aleatorio cada vez
```

### 3. Verificar Ofuscación
```bash
# Descomprimir APK release
unzip app/build/outputs/apk/release/app-release.apk

# Ver clases obfuscadas
dexdump -d classes.dex | grep "SentryCore"
# Debe mostrar: Lcom/odin/core/obfuscated/[random_name];

# Clases críticas NO obfuscadas (por ProGuard config)
dexdump -d classes.dex | grep "AppCompatActivity"
# Debe existir (no ofuscada por framework)
```

---

## 📊 BENCHMARK DE RENDIMIENTO

### 1. Medida CPU (Reposo)
```bash
# Conectar dispositivo
adb shell

# Monitorear CPU del proceso
dumpsys cpuinfo | grep odin.core
# Debe mostrar: <1% CPU

# O usar Battery Historian:
# 1. Recopilar: adb bugreport
# 2. Generar: battery-historian-master/bin/battery-historian battery.zip
# 3. Verificar: CPU time < 1%
```

### 2. Medida Memoria
```bash
adb shell dumpsys meminfo com.odin.core | grep "TOTAL"
# Memoria usada debe ser ~2-5 MB

# Verificar sin leaks
adb shell dumpsys meminfo --local-only com.odin.core
```

### 3. Medida Batería (8h)
```bash
# Usar Battery Historian o similar
# Esperado: <1% drenaje por 8 horas
```

---

## 🚨 TROUBLESHOOTING

### Error: "No se pudo inicializar SentryCore"
```
Causa: AndroidKeyStore no disponible
Solución: 
- Verificar minSdk >= 21 (para EncryptedSharedPreferences)
- Verificar que no estés en emulador sin soporte HW
```

### Error: "EncryptedSharedPreferences initialization failed"
```
Causa: Permissions faltando
Solución:
- Ejecutar: adb shell pm grant com.odin.core android.permission.WRITE_SECURE_SETTINGS
- O reinstalar app: adb uninstall && adb install
```

### Error: "WorkManager constraint not met"
```
Causa: WorkManager no puede ejecutarse
Solución:
- Verificar que há espacio en storage
- Verificar que WorkManager está en app/build.gradle
- Reiniciar dispositivo
```

### Error: "Corrupción detectada" en logs
```
Causa: Datos dañados al descifrar
Solución:
- SentryCore auto-sanación activada (ver logs)
- Datos corruptos fueron limpiados
- Sistema se recuperó automáticamente ✓
```

---

## 📋 CHECKLIST PRE-RELEASE

- [ ] ✅ Compilación sin errores (assembleRelease)
- [ ] ✅ ProGuard se ejecutó (verify mapping.txt)
- [ ] ✅ Tamaño APK < 5 MB
- [ ] ✅ Todos los tests pasan (connectedAndroidTest)
- [ ] ✅ SentryCore inicializa correctamente
- [ ] ✅ GlobalExceptionHandler registrado
- [ ] ✅ LogRotationWorker programado
- [ ] ✅ Verificar NO hay logs sensibles en Logcat
- [ ] ✅ Verificar datos cifrados en almacenamiento
- [ ] ✅ CPU < 1% en reposo
- [ ] ✅ Memoria < 5 MB
- [ ] ✅ Batería < 1% drenaje/8h

---

## 🎯 DEPLOYMENT A PRODUCCIÓN

### Paso 1: Generar Signed APK
```bash
# En Android Studio:
# 1. Build → Generate Signed Bundle/APK
# 2. Seleccionar: APK
# 3. Seleccionar keystore (crear si no existe)
# 4. Configurar: release build type
# 5. Generar: app-release.apk

# O mediante línea de comandos:
./gradlew assembleRelease
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore odin.keystore \
  app/build/outputs/apk/release/app-release.apk \
  odin_key

zipalign -v 4 app-release-unaligned.apk app-release-aligned.apk
```

### Paso 2: Verificar Firma
```bash
jarsigner -verify -verbose -certs app-release.apk
# Debe mostrar: jar verified
```

### Paso 3: Upload a Play Store
```
1. Google Play Console
2. Internal Testing Track
3. Upload APK
4. Esperar verificación (24-48h)
5. Dar acceso a testers internos
6. Monitorar Crashes & ANRs
7. Si todo OK → Alpha → Beta → Production
```

---

## 📈 POST-DEPLOYMENT MONITORING

### Métricas a Monitorar
- **Crash Rate**: < 0.1%
- **ANR Rate**: < 0.05%
- **Avg Session Duration**: > 5 min
- **CPU Usage**: < 1%
- **Memory Usage**: < 8 MB
- **Battery Drain**: < 2% / 8h

### Logs a Revisar
```
Monitorar en Firebase Crashlytics / Play Console:
- Security Exceptions: Debe ser 0
- Uncaught Exceptions: Debe ser < 0.1%
- OOM (Out of Memory): Debe ser 0
```

---

**ODIN SentryCore - Listo para Producción ✅**

Próximo Paso: **Fase 2 - NetShield (RED) - Octubre 2026**
