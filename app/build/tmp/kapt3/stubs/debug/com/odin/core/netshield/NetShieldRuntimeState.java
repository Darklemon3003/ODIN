package com.odin.core.netshield;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001:\u00012B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0007J\u0018\u0010\u001e\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020 H\u0007J\u001a\u0010!\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010#\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0007J \u0010$\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020 H\u0007J\u001a\u0010(\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010\u0006H\u0007J\u001a\u0010)\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010*\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0007J\u0018\u0010+\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010,\u001a\u00020 H\u0007J\u0018\u0010-\u001a\n /*\u0004\u0018\u00010.0.2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u00100\u001a\u0002012\u0006\u0010\u001c\u001a\u00020\u001dH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/odin/core/netshield/NetShieldRuntimeState;", "", "()V", "HEARTBEAT_TIMEOUT_MS", "", "KEY_BYTES_IN", "", "KEY_BYTES_OUT", "KEY_CONNECTIVITY_RISK_ACCEPTED", "KEY_LAST_ERROR", "KEY_LAST_HEARTBEAT", "KEY_LAST_PACKET_TIME", "KEY_LAST_STARTED", "KEY_LAST_STOPPED", "KEY_PACKETS_ALLOWED", "KEY_PACKETS_AUDITED", "KEY_PACKETS_BLOCKED", "KEY_SERVICE_RUNNING", "KEY_STATE", "KEY_VPN_AUTHORIZED", "PREFS_NAME", "STATE_ACTIVE", "STATE_ERROR", "STATE_INACTIVE", "STATE_STARTING", "STATE_STOPPING", "markActive", "", "context", "Landroid/content/Context;", "markConnectivityRiskAccepted", "accepted", "", "markError", "message", "markHeartbeat", "markPacketObserved", "bytes", "", "allowed", "markStarting", "markStopped", "markStopping", "markVpnAuthorized", "authorized", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "read", "Lcom/odin/core/netshield/NetShieldRuntimeState$Snapshot;", "Snapshot", "app_debug"})
public final class NetShieldRuntimeState {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREFS_NAME = "odin_netshield_runtime";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_STATE = "state";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SERVICE_RUNNING = "service_running";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_VPN_AUTHORIZED = "vpn_authorized";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_CONNECTIVITY_RISK_ACCEPTED = "connectivity_risk_accepted";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_HEARTBEAT = "last_heartbeat";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_STARTED = "last_started";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_STOPPED = "last_stopped";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_ERROR = "last_error";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PACKETS_AUDITED = "packets_audited";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PACKETS_ALLOWED = "packets_allowed";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PACKETS_BLOCKED = "packets_blocked";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_BYTES_IN = "bytes_in";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_BYTES_OUT = "bytes_out";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_PACKET_TIME = "last_packet_time";
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
    public static final com.odin.core.netshield.NetShieldRuntimeState INSTANCE = null;
    
    private NetShieldRuntimeState() {
        super();
    }
    
    @kotlin.jvm.JvmStatic()
    @org.jetbrains.annotations.NotNull()
    public static final com.odin.core.netshield.NetShieldRuntimeState.Snapshot read(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markVpnAuthorized(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean authorized) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markConnectivityRiskAccepted(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean accepted) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markStarting(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String message) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markActive(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markHeartbeat(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void markPacketObserved(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int bytes, boolean allowed) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b/\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Bw\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0006\u0010\u000b\u001a\u00020\t\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\r\u001a\u00020\t\u0012\u0006\u0010\u000e\u001a\u00020\t\u0012\u0006\u0010\u000f\u001a\u00020\t\u0012\u0006\u0010\u0010\u001a\u00020\t\u0012\u0006\u0010\u0011\u001a\u00020\t\u0012\u0006\u0010\u0012\u001a\u00020\t\u00a2\u0006\u0002\u0010\u0013J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\tH\u00c6\u0003J\t\u0010\'\u001a\u00020\tH\u00c6\u0003J\t\u0010(\u001a\u00020\tH\u00c6\u0003J\t\u0010)\u001a\u00020\tH\u00c6\u0003J\t\u0010*\u001a\u00020\tH\u00c6\u0003J\t\u0010+\u001a\u00020\u0005H\u00c6\u0003J\t\u0010,\u001a\u00020\u0005H\u00c6\u0003J\t\u0010-\u001a\u00020\u0005H\u00c6\u0003J\t\u0010.\u001a\u00020\tH\u00c6\u0003J\t\u0010/\u001a\u00020\tH\u00c6\u0003J\t\u00100\u001a\u00020\tH\u00c6\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u00102\u001a\u00020\tH\u00c6\u0003J\u0097\u0001\u00103\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\t2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\r\u001a\u00020\t2\b\b\u0002\u0010\u000e\u001a\u00020\t2\b\b\u0002\u0010\u000f\u001a\u00020\t2\b\b\u0002\u0010\u0010\u001a\u00020\t2\b\b\u0002\u0010\u0011\u001a\u00020\t2\b\b\u0002\u0010\u0012\u001a\u00020\tH\u00c6\u0001J\u0013\u00104\u001a\u00020\u00052\b\u00105\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u0010\u00106\u001a\u00020\u00052\b\b\u0002\u00107\u001a\u00020\tJ\t\u00108\u001a\u000209H\u00d6\u0001J\t\u0010:\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0010\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0011\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0015R\u0011\u0010\u0012\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0015R\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0015R\u0011\u0010\u000b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0015R\u0011\u0010\u000e\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0015R\u0011\u0010\r\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0015R\u0011\u0010\u000f\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0018R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001aR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0018\u00a8\u0006;"}, d2 = {"Lcom/odin/core/netshield/NetShieldRuntimeState$Snapshot;", "", "state", "", "serviceRunning", "", "vpnAuthorized", "connectivityRiskAccepted", "lastHeartbeat", "", "lastStarted", "lastStopped", "lastError", "packetsAudited", "packetsAllowed", "packetsBlocked", "bytesIn", "bytesOut", "lastPacketTime", "(Ljava/lang/String;ZZZJJJLjava/lang/String;JJJJJJ)V", "getBytesIn", "()J", "getBytesOut", "getConnectivityRiskAccepted", "()Z", "getLastError", "()Ljava/lang/String;", "getLastHeartbeat", "getLastPacketTime", "getLastStarted", "getLastStopped", "getPacketsAllowed", "getPacketsAudited", "getPacketsBlocked", "getServiceRunning", "getState", "getVpnAuthorized", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hasFreshHeartbeat", "now", "hashCode", "", "toString", "app_debug"})
    public static final class Snapshot {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String state = null;
        private final boolean serviceRunning = false;
        private final boolean vpnAuthorized = false;
        private final boolean connectivityRiskAccepted = false;
        private final long lastHeartbeat = 0L;
        private final long lastStarted = 0L;
        private final long lastStopped = 0L;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String lastError = null;
        private final long packetsAudited = 0L;
        private final long packetsAllowed = 0L;
        private final long packetsBlocked = 0L;
        private final long bytesIn = 0L;
        private final long bytesOut = 0L;
        private final long lastPacketTime = 0L;
        
        public Snapshot(@org.jetbrains.annotations.NotNull()
        java.lang.String state, boolean serviceRunning, boolean vpnAuthorized, boolean connectivityRiskAccepted, long lastHeartbeat, long lastStarted, long lastStopped, @org.jetbrains.annotations.Nullable()
        java.lang.String lastError, long packetsAudited, long packetsAllowed, long packetsBlocked, long bytesIn, long bytesOut, long lastPacketTime) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getState() {
            return null;
        }
        
        public final boolean getServiceRunning() {
            return false;
        }
        
        public final boolean getVpnAuthorized() {
            return false;
        }
        
        public final boolean getConnectivityRiskAccepted() {
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
        
        public final long getPacketsAudited() {
            return 0L;
        }
        
        public final long getPacketsAllowed() {
            return 0L;
        }
        
        public final long getPacketsBlocked() {
            return 0L;
        }
        
        public final long getBytesIn() {
            return 0L;
        }
        
        public final long getBytesOut() {
            return 0L;
        }
        
        public final long getLastPacketTime() {
            return 0L;
        }
        
        public final boolean hasFreshHeartbeat(long now) {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        public final long component10() {
            return 0L;
        }
        
        public final long component11() {
            return 0L;
        }
        
        public final long component12() {
            return 0L;
        }
        
        public final long component13() {
            return 0L;
        }
        
        public final long component14() {
            return 0L;
        }
        
        public final boolean component2() {
            return false;
        }
        
        public final boolean component3() {
            return false;
        }
        
        public final boolean component4() {
            return false;
        }
        
        public final long component5() {
            return 0L;
        }
        
        public final long component6() {
            return 0L;
        }
        
        public final long component7() {
            return 0L;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component8() {
            return null;
        }
        
        public final long component9() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.odin.core.netshield.NetShieldRuntimeState.Snapshot copy(@org.jetbrains.annotations.NotNull()
        java.lang.String state, boolean serviceRunning, boolean vpnAuthorized, boolean connectivityRiskAccepted, long lastHeartbeat, long lastStarted, long lastStopped, @org.jetbrains.annotations.Nullable()
        java.lang.String lastError, long packetsAudited, long packetsAllowed, long packetsBlocked, long bytesIn, long bytesOut, long lastPacketTime) {
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