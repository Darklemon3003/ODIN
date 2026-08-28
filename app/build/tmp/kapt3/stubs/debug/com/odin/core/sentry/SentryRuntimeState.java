package com.odin.core.sentry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001:\u0001$B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u001a\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u0006H\u0007J\u0018\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u001a\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u0006H\u0007J\u001a\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u0018\u0010\u001f\u001a\n !*\u0004\u0018\u00010 0 2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\u0015\u001a\u00020\u0016H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/odin/core/sentry/SentryRuntimeState;", "", "()V", "HEARTBEAT_TIMEOUT_MS", "", "KEY_CORE_HEALTHY", "", "KEY_LAST_ERROR", "KEY_LAST_HEARTBEAT", "KEY_LAST_STARTED", "KEY_LAST_STOPPED", "KEY_SERVICE_RUNNING", "KEY_STATE", "PREFS_NAME", "STATE_ACTIVE", "STATE_ERROR", "STATE_INACTIVE", "STATE_STARTING", "STATE_STOPPING", "markActive", "", "context", "Landroid/content/Context;", "coreHealthy", "", "markError", "message", "markHeartbeat", "markStarting", "markStopped", "markStopping", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "read", "Lcom/odin/core/sentry/SentryRuntimeState$Snapshot;", "Snapshot", "app_debug"})
public final class SentryRuntimeState {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "odin_sentry_runtime";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_STATE = "state";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SERVICE_RUNNING = "service_running";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_HEARTBEAT = "last_heartbeat";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_STARTED = "last_started";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_STOPPED = "last_stopped";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_ERROR = "last_error";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_CORE_HEALTHY = "core_healthy";
    private static final long HEARTBEAT_TIMEOUT_MS = 90000L;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_INACTIVE = "INACTIVE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_STARTING = "STARTING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_ACTIVE = "ACTIVE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_STOPPING = "STOPPING";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String STATE_ERROR = "ERROR";
    @org.jetbrains.annotations.NotNull()
    public static final com.odin.core.sentry.SentryRuntimeState INSTANCE = null;
    
    private SentryRuntimeState() {
        super();
    }
    
    @kotlin.jvm.JvmStatic()
    @org.jetbrains.annotations.NotNull()
    public static final com.odin.core.sentry.SentryRuntimeState.Snapshot read(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markStarting(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String message) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markActive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean coreHealthy) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markHeartbeat(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean coreHealthy) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markStopping(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markStopped(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String message) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markError(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String message) {
    }
    
    private final android.content.SharedPreferences prefs(android.content.Context context) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u001c\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0007\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003JQ\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020\u00052\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u0010\u0010!\u001a\u00020\u00052\b\b\u0002\u0010\"\u001a\u00020\u0007J\t\u0010#\u001a\u00020$H\u00d6\u0001J\t\u0010%\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\n\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010\u00a8\u0006&"}, d2 = {"Lcom/odin/core/sentry/SentryRuntimeState$Snapshot;", "", "state", "", "serviceRunning", "", "lastHeartbeat", "", "lastStarted", "lastStopped", "lastError", "coreHealthy", "(Ljava/lang/String;ZJJJLjava/lang/String;Z)V", "getCoreHealthy", "()Z", "getLastError", "()Ljava/lang/String;", "getLastHeartbeat", "()J", "getLastStarted", "getLastStopped", "getServiceRunning", "getState", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hasFreshHeartbeat", "now", "hashCode", "", "toString", "app_debug"})
    public static final class Snapshot {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String state = null;
        private final boolean serviceRunning = false;
        private final long lastHeartbeat = 0L;
        private final long lastStarted = 0L;
        private final long lastStopped = 0L;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String lastError = null;
        private final boolean coreHealthy = false;
        
        public Snapshot(@org.jetbrains.annotations.NotNull()
        java.lang.String state, boolean serviceRunning, long lastHeartbeat, long lastStarted, long lastStopped, @org.jetbrains.annotations.Nullable()
        java.lang.String lastError, boolean coreHealthy) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getState() {
            return null;
        }
        
        public final boolean getServiceRunning() {
            return false;
        }
        
        public final long getLastHeartbeat() {
            return 0L;
        }
        
        public final long getLastStarted() {
            return 0L;
        }
        
        public final long getLastStopped() {
            return 0L;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getLastError() {
            return null;
        }
        
        public final boolean getCoreHealthy() {
            return false;
        }
        
        public final boolean hasFreshHeartbeat(long now) {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        public final boolean component2() {
            return false;
        }
        
        public final long component3() {
            return 0L;
        }
        
        public final long component4() {
            return 0L;
        }
        
        public final long component5() {
            return 0L;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component6() {
            return null;
        }
        
        public final boolean component7() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.sentry.SentryRuntimeState.Snapshot copy(@org.jetbrains.annotations.NotNull()
        java.lang.String state, boolean serviceRunning, long lastHeartbeat, long lastStarted, long lastStopped, @org.jetbrains.annotations.Nullable()
        java.lang.String lastError, boolean coreHealthy) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}