package com.odin.core.ui.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b(\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0014J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0003H\u00c6\u0003J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\t\u0010*\u001a\u00020\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\t\u0010-\u001a\u00020\tH\u00c6\u0003J\t\u0010.\u001a\u00020\u000bH\u00c6\u0003J\u0010\u0010/\u001a\u0004\u0018\u00010\rH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001bJ\u000b\u00100\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u009e\u0001\u00101\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u0003H\u00c6\u0001\u00a2\u0006\u0002\u00102J\u0013\u00103\u001a\u00020\u00032\b\u00104\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00105\u001a\u000206H\u00d6\u0001J\t\u00107\u001a\u00020\u000bH\u00d6\u0001R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0016R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0016R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0016R\u0015\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0016R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0016R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0019\u00a8\u00068"}, d2 = {"Lcom/odin/core/ui/model/SentryUiState;", "", "isConsentGranted", "", "isBootConsentGranted", "isNotificationPermissionGranted", "isServiceRunning", "isCoreHealthy", "status", "Lcom/odin/core/ui/model/SentryStatus;", "statusMessage", "", "lastHeartbeat", "", "errorMessage", "canStart", "canStop", "requiresConsent", "requiresNotificationPermission", "requiresBootConsent", "(ZZZZZLcom/odin/core/ui/model/SentryStatus;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;ZZZZZ)V", "getCanStart", "()Z", "getCanStop", "getErrorMessage", "()Ljava/lang/String;", "getLastHeartbeat", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getRequiresBootConsent", "getRequiresConsent", "getRequiresNotificationPermission", "getStatus", "()Lcom/odin/core/ui/model/SentryStatus;", "getStatusMessage", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ZZZZZLcom/odin/core/ui/model/SentryStatus;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;ZZZZZ)Lcom/odin/core/ui/model/SentryUiState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class SentryUiState {
    private final boolean isConsentGranted = false;
    private final boolean isBootConsentGranted = false;
    private final boolean isNotificationPermissionGranted = false;
    private final boolean isServiceRunning = false;
    private final boolean isCoreHealthy = false;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.SentryStatus status = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String statusMessage = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastHeartbeat = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    private final boolean canStart = false;
    private final boolean canStop = false;
    private final boolean requiresConsent = false;
    private final boolean requiresNotificationPermission = false;
    private final boolean requiresBootConsent = false;
    
    public SentryUiState(boolean isConsentGranted, boolean isBootConsentGranted, boolean isNotificationPermissionGranted, boolean isServiceRunning, boolean isCoreHealthy, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.SentryStatus status, @org.jetbrains.annotations.NotNull()
    java.lang.String statusMessage, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastHeartbeat, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, boolean canStart, boolean canStop, boolean requiresConsent, boolean requiresNotificationPermission, boolean requiresBootConsent) {
        super();
    }
    
    public final boolean isConsentGranted() {
        return false;
    }
    
    public final boolean isBootConsentGranted() {
        return false;
    }
    
    public final boolean isNotificationPermissionGranted() {
        return false;
    }
    
    public final boolean isServiceRunning() {
        return false;
    }
    
    public final boolean isCoreHealthy() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.SentryStatus getStatus() {
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
    public final java.lang.String getErrorMessage() {
        return null;
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
    
    public final boolean getRequiresNotificationPermission() {
        return false;
    }
    
    public final boolean getRequiresBootConsent() {
        return false;
    }
    
    public SentryUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final boolean component14() {
        return false;
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
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.SentryStatus component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.SentryUiState copy(boolean isConsentGranted, boolean isBootConsentGranted, boolean isNotificationPermissionGranted, boolean isServiceRunning, boolean isCoreHealthy, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.SentryStatus status, @org.jetbrains.annotations.NotNull()
    java.lang.String statusMessage, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastHeartbeat, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage, boolean canStart, boolean canStop, boolean requiresConsent, boolean requiresNotificationPermission, boolean requiresBootConsent) {
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