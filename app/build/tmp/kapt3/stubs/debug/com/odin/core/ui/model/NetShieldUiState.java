package com.odin.core.ui.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b,\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u00cb\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0019\u00a2\u0006\u0002\u0010\u001aJ\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010/\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003\u00a2\u0006\u0002\u0010\"J\u0010\u00100\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003\u00a2\u0006\u0002\u0010\"J\u000b\u00101\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u0003H\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0019H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u0003H\u00c6\u0003J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\t\u0010>\u001a\u00020\nH\u00c6\u0003J\t\u0010?\u001a\u00020\fH\u00c6\u0003J\u0010\u0010@\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003\u00a2\u0006\u0002\u0010\"J\u00d4\u0001\u0010A\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u00c6\u0001\u00a2\u0006\u0002\u0010BJ\u0013\u0010C\u001a\u00020\u00032\b\u0010D\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010E\u001a\u00020FH\u00d6\u0001J\t\u0010G\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0013\u0010\u0011\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u001cR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u001cR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u001cR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u001cR\u0015\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\n\n\u0002\u0010#\u001a\u0004\b!\u0010\"R\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\n\n\u0002\u0010#\u001a\u0004\b$\u0010\"R\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\n\n\u0002\u0010#\u001a\u0004\b%\u0010\"R\u0011\u0010\u0018\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001cR\u0011\u0010\u0017\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001cR\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001cR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010 \u00a8\u0006H"}, d2 = {"Lcom/odin/core/ui/model/NetShieldUiState;", "", "isConsentGranted", "", "isVpnAuthorized", "isNotificationPermissionGranted", "isConnectivityRiskAccepted", "isServiceRunning", "isSafeValidationMode", "status", "Lcom/odin/core/ui/model/NetShieldStatus;", "statusMessage", "", "lastHeartbeat", "", "lastStartedAt", "lastStoppedAt", "errorMessage", "canRequestVpnAuthorization", "canStart", "canStop", "requiresConsent", "requiresVpnAuthorization", "requiresNotificationPermission", "metrics", "Lcom/odin/core/ui/model/NetShieldMetrics;", "(ZZZZZZLcom/odin/core/ui/model/NetShieldStatus;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;ZZZZZZLcom/odin/core/ui/model/NetShieldMetrics;)V", "getCanRequestVpnAuthorization", "()Z", "getCanStart", "getCanStop", "getErrorMessage", "()Ljava/lang/String;", "getLastHeartbeat", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getLastStartedAt", "getLastStoppedAt", "getMetrics", "()Lcom/odin/core/ui/model/NetShieldMetrics;", "getRequiresConsent", "getRequiresNotificationPermission", "getRequiresVpnAuthorization", "getStatus", "()Lcom/odin/core/ui/model/NetShieldStatus;", "getStatusMessage", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ZZZZZZLcom/odin/core/ui/model/NetShieldStatus;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;ZZZZZZLcom/odin/core/ui/model/NetShieldMetrics;)Lcom/odin/core/ui/model/NetShieldUiState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class NetShieldUiState {
    private final boolean isConsentGranted = false;
    private final boolean isVpnAuthorized = false;
    private final boolean isNotificationPermissionGranted = false;
    private final boolean isConnectivityRiskAccepted = false;
    private final boolean isServiceRunning = false;
    private final boolean isSafeValidationMode = false;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.NetShieldStatus status = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String statusMessage = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastHeartbeat = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastStartedAt = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastStoppedAt = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    private final boolean canRequestVpnAuthorization = false;
    private final boolean canStart = false;
    private final boolean canStop = false;
    private final boolean requiresConsent = false;
    private final boolean requiresVpnAuthorization = false;
    private final boolean requiresNotificationPermission = false;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.NetShieldMetrics metrics = null;
    
    public NetShieldUiState(boolean isConsentGranted, boolean isVpnAuthorized, boolean isNotificationPermissionGranted, boolean isConnectivityRiskAccepted, boolean isServiceRunning, boolean isSafeValidationMode, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.NetShieldStatus status, @org.jetbrains.annotations.NotNull()
    java.lang.String statusMessage, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastHeartbeat, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastStartedAt, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastStoppedAt, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, boolean canRequestVpnAuthorization, boolean canStart, boolean canStop, boolean requiresConsent, boolean requiresVpnAuthorization, boolean requiresNotificationPermission, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.NetShieldMetrics metrics) {
        super();
    }
    
    public final boolean isConsentGranted() {
        return false;
    }
    
    public final boolean isVpnAuthorized() {
        return false;
    }
    
    public final boolean isNotificationPermissionGranted() {
        return false;
    }
    
    public final boolean isConnectivityRiskAccepted() {
        return false;
    }
    
    public final boolean isServiceRunning() {
        return false;
    }
    
    public final boolean isSafeValidationMode() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.NetShieldStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getStatusMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getLastHeartbeat() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getLastStartedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getLastStoppedAt() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public final boolean getCanRequestVpnAuthorization() {
        return false;
    }
    
    public final boolean getCanStart() {
        return false;
    }
    
    public final boolean getCanStop() {
        return false;
    }
    
    public final boolean getRequiresConsent() {
        return false;
    }
    
    public final boolean getRequiresVpnAuthorization() {
        return false;
    }
    
    public final boolean getRequiresNotificationPermission() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.NetShieldMetrics getMetrics() {
        return null;
    }
    
    public NetShieldUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final boolean component14() {
        return false;
    }
    
    public final boolean component15() {
        return false;
    }
    
    public final boolean component16() {
        return false;
    }
    
    public final boolean component17() {
        return false;
    }
    
    public final boolean component18() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.NetShieldMetrics component19() {
        return null;
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
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean component6() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.NetShieldStatus component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.NetShieldUiState copy(boolean isConsentGranted, boolean isVpnAuthorized, boolean isNotificationPermissionGranted, boolean isConnectivityRiskAccepted, boolean isServiceRunning, boolean isSafeValidationMode, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.NetShieldStatus status, @org.jetbrains.annotations.NotNull()
    java.lang.String statusMessage, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastHeartbeat, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastStartedAt, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastStoppedAt, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, boolean canRequestVpnAuthorization, boolean canStart, boolean canStop, boolean requiresConsent, boolean requiresVpnAuthorization, boolean requiresNotificationPermission, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.NetShieldMetrics metrics) {
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