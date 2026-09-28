# ============================================
# ODIN Project - ProGuard Configuration
# Obfuscación Industrial para evitar Ingeniería Inversa
# ============================================

# ============================================
# 1. CONFIGURACIÓN GENERAL
# ============================================
-verbose
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Ofuscación agresiva
# Note: Disabled obfuscation dictionaries for Windows compatibility
# -obfuscationdictionary /dev/null
# -classobfuscationdictionary /dev/null
# -packageobfuscationdictionary /dev/null
-allowaccessmodification
-repackageclasses 'com.odin.core.obfuscated'

# ============================================
# 2. MANTENER CLASES Y MIEMBROS CRÍTICOS
# ============================================

# Core ODIN - No ofuscar interfaces públicas pero sí implementaciones
-keep class com.odin.core.exception.** { *; }
-keep class com.odin.core.security.CryptographicManager { 
    public <init>(...);
    public synchronized java.lang.String encrypt(...);
    public synchronized java.lang.String decrypt(...);
    public boolean isKeyAvailable();
}

-keep class com.odin.core.persistence.SentryCore {
    public static com.odin.core.persistence.SentryCore getInstance(...);
    public synchronized void appendEvent(...);
    public synchronized java.util.List fetchRecentEvents(int);
    public boolean isHealthy();
    public long getLastHeartbeat();
    public long getCrashCount();
}

-keep class com.odin.core.persistence.LogRotationManager {
    public static void scheduleLogRotation(...);
    public static void cancelLogRotation(...);
}

# Service - Mantener nombres públicos para manifest
-keep class com.odin.core.service.** {
    <init>();
}

-keep class com.odin.core.service.SentryService {
    public <init>();
    public int onStartCommand(...);
    public void onCreate();
    public void onDestroy();
}

-keep class com.odin.core.service.NetShieldVpnService {
    public <init>();
    public int onStartCommand(...);
}

# BroadcastReceiver - Mantener para manifest
-keep class com.odin.core.receiver.BootReceiver {
    public <init>();
    public void onReceive(...);
}

# MainActivity - Mantener para manifest
-keep class com.odin.core.ui.MainActivity {
    public <init>();
}

# Data Models - Inmutables, mantener estructura
-keep class com.odin.core.domain.model.** {
    <init>(...);
    public *** get*();
    public *** is*();
}

# Repositories - Mantener interfaces del dominio
-keep interface com.odin.core.domain.repository.** {
    *** *(...);
}

# ============================================
# 3. ENCRIPTEDSHAREDPREFERENCES (Androidx.security)
# ============================================
-keep class androidx.security.crypto.EncryptedSharedPreferences { *; }
-keep class androidx.security.crypto.MasterKey { *; }
-keep class androidx.security.crypto.MasterKey$Builder { *; }

# ============================================
# 4. WORKMANAGER (Androidx.work)
# ============================================
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.ListenableWorker { *; }

# ============================================
# 5. ANDROIDX CORE
# ============================================
-keep class androidx.core.** { *; }
-keep class androidx.appcompat.** { *; }
-keep class com.google.android.material.** { *; }

# ============================================
# 6. NOTIFICACIONES (Androidx.notification)
# ============================================
-keep class androidx.core.app.NotificationCompat { *; }
-keep class androidx.core.app.NotificationCompat$Builder { *; }

# ============================================
# 7. ANDROID FRAMEWORK (No ofuscar)
# ============================================
-keep class android.** { *; }
-keep interface android.** { *; }

# ============================================
# 8. OFUSCAR IMPLEMENTACIONES
# ============================================

# Ofuscar repositorios de implementación (data layer)
-keep class com.odin.core.data.repository.** { *; }
-keep class com.odin.core.data.local.** { *; }

# Ofuscar DI
-keep class com.odin.core.di.** {
    public static *** provide*(...);
}

# ============================================
# 9. MANEJO DE EXCEPCIONES
# ============================================
-keep class * extends java.lang.Exception {
    <init>();
    <init>(java.lang.String);
    <init>(java.lang.String, java.lang.Throwable);
}

# ============================================
# 10. OPTIMIZACIONES FINALES
# ============================================

# Remover logs — all ODIN production Log calls stripped in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Remover métodos assert
-assumenosideeffects class java.lang.Throwable {
    public void printStackTrace(...);
}

# ============================================
# 11. CONFIGURACIÓN DE WARNINGS
# ============================================
-dontwarn androidx.**
-dontwarn com.google.android.material.**
-dontwarn android.**
-dontwarn java.lang.invoke.**
-dontwarn javax.lang.model.**
-dontwarn sun.misc.Unsafe
-dontwarn sun.reflect.**

# ============================================
# 12. JETPACK COMPOSE (Sprint 6 UI/UX Overhaul)
# ============================================
# Keep Compose classes
-keep class androidx.compose.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.material.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.animation.** { *; }
-keep class androidx.compose.runtime.** { *; }

# Keep Compose generated classes
-keep class androidx.compose.** { *; }
-keep class androidx.compose.ui.** { *; }
-dontwarn androidx.compose.**

# ============================================
# 13. HILT DEPENDENCY INJECTION (Sprint 6)
# ============================================
# Keep Hilt classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$ActivityContextWrapper { *; }
-dontwarn dagger.hilt.**

# Keep Hilt generated classes
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$ActivityContextWrapper { *; }

# ============================================
# 14. ROOM DATABASE (Sprint 6)
# ============================================
# Keep Room classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.**
-keep @androidx.room.Dao class *
-keep class * extends androidx.room.Dao

# ============================================
# 15. VIEWMODEL AND LIFECYCLE (Sprint 6)
# ============================================
# Keep ViewModel classes
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

# ============================================
# 16. NAVIGATION COMPOSE (Sprint 6)
# ============================================
# Keep Navigation classes
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

# ============================================
# 17. KOTLIN COROUTINES (Sprint 6)
# ============================================
# Keep Coroutines classes
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ============================================
# 18. SERIALIZATION AND REFLECTION (Sprint 6)
# ============================================
# Keep serialization classes
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable classes
-keep class * implements android.os.Parcelable { *; }
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ============================================
# 19. DATA CLASSES (Sprint 6)
# ============================================
# Keep data classes for domain and data layers
-keep class com.odin.core.domain.model.** { *; }
-keep class com.odin.core.data.model.** { *; }
-keep class com.odin.core.data.repository.** { *; }

# ============================================
# 20. DATA CLASSES (Sprint 6)
# ============================================
# Keep data classes for domain and data layers
-keep class com.odin.core.domain.model.** { *; }
-keep class com.odin.core.data.model.** { *; }
-keep class com.odin.core.data.repository.** { *; }

# ============================================
# 21. TINK (transitive via security-crypto) — missing optional HTTP downloader classes
# ============================================
-dontwarn com.google.api.client.http.GenericUrl
-dontwarn com.google.api.client.http.HttpHeaders
-dontwarn com.google.api.client.http.HttpRequest
-dontwarn com.google.api.client.http.HttpRequestFactory
-dontwarn com.google.api.client.http.HttpResponse
-dontwarn com.google.api.client.http.HttpTransport
-dontwarn com.google.api.client.http.javanet.NetHttpTransport$Builder
-dontwarn com.google.api.client.http.javanet.NetHttpTransport
-dontwarn org.joda.time.Instant

