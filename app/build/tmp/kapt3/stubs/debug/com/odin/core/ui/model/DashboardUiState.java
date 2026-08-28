package com.odin.core.ui.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b-\b\u0087\b\u0018\u00002\u00020\u0001B\u00af\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\b\b\u0002\u0010\u000b\u001a\u00020\t\u0012\b\b\u0002\u0010\f\u001a\u00020\t\u0012\b\b\u0002\u0010\r\u001a\u00020\u0007\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u00a2\u0006\u0002\u0010\u001bJ\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\u000f\u00104\u001a\b\u0012\u0004\u0012\u00020\u00120\u000fH\u00c6\u0003J\u000f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00140\u000fH\u00c6\u0003J\u000f\u00106\u001a\b\u0012\u0004\u0012\u00020\u00160\u000fH\u00c6\u0003J\u0010\u00107\u001a\u0004\u0018\u00010\u0018H\u00c6\u0003\u00a2\u0006\u0002\u0010#J\u000b\u00108\u001a\u0004\u0018\u00010\u001aH\u00c6\u0003J\t\u00109\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010:\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010,J\t\u0010;\u001a\u00020\tH\u00c6\u0003J\t\u0010<\u001a\u00020\tH\u00c6\u0003J\t\u0010=\u001a\u00020\tH\u00c6\u0003J\t\u0010>\u001a\u00020\tH\u00c6\u0003J\t\u0010?\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u00c6\u0003J\u00b8\u0001\u0010A\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\t2\b\b\u0002\u0010\r\u001a\u00020\u00072\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f2\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00182\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u00c6\u0001\u00a2\u0006\u0002\u0010BJ\u0013\u0010C\u001a\u00020\u00032\b\u0010D\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010E\u001a\u00020\u0007H\u00d6\u0001J\t\u0010F\u001a\u00020\u001aH\u00d6\u0001R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001dR\u0013\u0010\u0019\u001a\u0004\u0018\u00010\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010!R\u0015\u0010\u0017\u001a\u0004\u0018\u00010\u0018\u00a2\u0006\n\n\u0002\u0010$\u001a\u0004\b\"\u0010#R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001dR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\f\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\'R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\n\n\u0002\u0010-\u001a\u0004\b+\u0010,R\u0011\u0010\r\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\u001dR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\'R\u0011\u0010\u000b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\'\u00a8\u0006G"}, d2 = {"Lcom/odin/core/ui/model/DashboardUiState;", "", "isLoading", "", "overallStatus", "Lcom/odin/core/ui/model/OverallStatus;", "postureScore", "", "sentryStatus", "Lcom/odin/core/ui/model/ModuleStatus;", "netShieldStatus", "vpnStatus", "permissionsStatus", "recentAlertsCount", "recentEvents", "", "Lcom/odin/core/domain/model/LogEvent;", "modules", "Lcom/odin/core/ui/model/DashboardModuleState;", "blockedFeatures", "Lcom/odin/core/ui/model/DashboardBlockedFeature;", "availableActions", "Lcom/odin/core/ui/model/DashboardQuickAction;", "lastUpdated", "", "errorMessage", "", "(ZLcom/odin/core/ui/model/OverallStatus;Ljava/lang/Integer;Lcom/odin/core/ui/model/ModuleStatus;Lcom/odin/core/ui/model/ModuleStatus;Lcom/odin/core/ui/model/ModuleStatus;Lcom/odin/core/ui/model/ModuleStatus;ILjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/Long;Ljava/lang/String;)V", "getAvailableActions", "()Ljava/util/List;", "getBlockedFeatures", "getErrorMessage", "()Ljava/lang/String;", "()Z", "getLastUpdated", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getModules", "getNetShieldStatus", "()Lcom/odin/core/ui/model/ModuleStatus;", "getOverallStatus", "()Lcom/odin/core/ui/model/OverallStatus;", "getPermissionsStatus", "getPostureScore", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getRecentAlertsCount", "()I", "getRecentEvents", "getSentryStatus", "getVpnStatus", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ZLcom/odin/core/ui/model/OverallStatus;Ljava/lang/Integer;Lcom/odin/core/ui/model/ModuleStatus;Lcom/odin/core/ui/model/ModuleStatus;Lcom/odin/core/ui/model/ModuleStatus;Lcom/odin/core/ui/model/ModuleStatus;ILjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/Long;Ljava/lang/String;)Lcom/odin/core/ui/model/DashboardUiState;", "equals", "other", "hashCode", "toString", "app_debug"})
public final class DashboardUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.OverallStatus overallStatus = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer postureScore = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.ModuleStatus sentryStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.ModuleStatus netShieldStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.ModuleStatus vpnStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.ui.model.ModuleStatus permissionsStatus = null;
    private final int recentAlertsCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.domain.model.LogEvent> recentEvents = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.ui.model.DashboardModuleState> modules = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.ui.model.DashboardBlockedFeature> blockedFeatures = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.odin.core.ui.model.DashboardQuickAction> availableActions = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastUpdated = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    public DashboardUiState(boolean isLoading, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.OverallStatus overallStatus, @org.jetbrains.annotations.Nullable()
    java.lang.Integer postureScore, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus sentryStatus, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus netShieldStatus, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus vpnStatus, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus permissionsStatus, int recentAlertsCount, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.LogEvent> recentEvents, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.DashboardModuleState> modules, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.DashboardBlockedFeature> blockedFeatures, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.DashboardQuickAction> availableActions, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastUpdated, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.OverallStatus getOverallStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getPostureScore() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus getSentryStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus getNetShieldStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus getVpnStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus getPermissionsStatus() {
        return null;
    }
    
    public final int getRecentAlertsCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.LogEvent> getRecentEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.DashboardModuleState> getModules() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.DashboardBlockedFeature> getBlockedFeatures() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.DashboardQuickAction> getAvailableActions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getLastUpdated() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public DashboardUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.DashboardModuleState> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.DashboardBlockedFeature> component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.ui.model.DashboardQuickAction> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.OverallStatus component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.ModuleStatus component7() {
        return null;
    }
    
    public final int component8() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.odin.core.domain.model.LogEvent> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.odin.core.ui.model.DashboardUiState copy(boolean isLoading, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.OverallStatus overallStatus, @org.jetbrains.annotations.Nullable()
    java.lang.Integer postureScore, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus sentryStatus, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus netShieldStatus, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus vpnStatus, @org.jetbrains.annotations.NotNull()
    com.odin.core.ui.model.ModuleStatus permissionsStatus, int recentAlertsCount, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.domain.model.LogEvent> recentEvents, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.DashboardModuleState> modules, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.DashboardBlockedFeature> blockedFeatures, @org.jetbrains.annotations.NotNull()
    java.util.List<com.odin.core.ui.model.DashboardQuickAction> availableActions, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastUpdated, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
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