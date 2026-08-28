package com.odin.core.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0002JF\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u000e\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u000fH\u0002J(\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020&H\u0002J\b\u0010\'\u001a\u00020\u001dH\u0002J\b\u0010(\u001a\u00020)H\u0002J\"\u0010*\u001a\b\u0012\u0004\u0012\u00020+0\u000f2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020.0-H\u0082@\u00a2\u0006\u0002\u0010/J\u0006\u00100\u001a\u00020)J\f\u00101\u001a\u00020.*\u00020\u0014H\u0002J\f\u00102\u001a\u00020\u0014*\u00020\u0019H\u0002J\f\u00102\u001a\u00020\u0014*\u00020\u0016H\u0002J\f\u00103\u001a\u00020\u0014*\u00020\u0019H\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u00064"}, d2 = {"Lcom/odin/core/ui/viewmodel/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "logsRepository", "Lcom/odin/core/domain/repository/LogsRepository;", "(Landroid/content/Context;Lcom/odin/core/domain/repository/LogsRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/odin/core/ui/model/DashboardUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "buildBlockedFeatures", "", "Lcom/odin/core/ui/model/DashboardBlockedFeature;", "buildModuleStates", "Lcom/odin/core/ui/model/DashboardModuleState;", "sentryStatus", "Lcom/odin/core/ui/model/ModuleStatus;", "sentryState", "Lcom/odin/core/ui/model/SentryUiState;", "netShieldStatus", "netShieldState", "Lcom/odin/core/ui/model/NetShieldUiState;", "vpnStatus", "permissionsStatus", "logsAvailable", "", "buildQuickActions", "Lcom/odin/core/ui/model/DashboardQuickAction;", "calculateOverallStatus", "Lcom/odin/core/ui/model/OverallStatus;", "sentryServiceRunning", "netShieldServiceRunning", "notificationPermissionGranted", "recentAlertsCount", "", "hasNotificationPermission", "loadDashboardData", "", "loadRecentEvents", "Lcom/odin/core/domain/model/LogEvent;", "errors", "", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshData", "label", "toDashboardModuleStatus", "toDashboardVpnStatus", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.repository.LogsRepository logsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.odin.core.ui.model.DashboardUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.odin.core.ui.model.DashboardUiState> uiState = null;
    
    @javax.inject.Inject()
    public DashboardViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.odin.core.domain.repository.LogsRepository logsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.odin.core.ui.model.DashboardUiState> getUiState() {
        return null;
    }
    
    public final void refreshData() {
    }
    
    private final void loadDashboardData() {
    }
    
    private final java.lang.Object loadRecentEvents(java.util.List<java.lang.String> errors, kotlin.coroutines.Continuation<? super java.util.List<com.odin.core.domain.model.LogEvent>> $completion) {
        return null;
    }
    
    private final boolean hasNotificationPermission() {
        return false;
    }
    
    private final com.odin.core.ui.model.OverallStatus calculateOverallStatus(boolean sentryServiceRunning, boolean netShieldServiceRunning, boolean notificationPermissionGranted, int recentAlertsCount) {
        return null;
    }
    
    private final java.util.List<com.odin.core.ui.model.DashboardModuleState> buildModuleStates(com.odin.core.ui.model.ModuleStatus sentryStatus, com.odin.core.ui.model.SentryUiState sentryState, com.odin.core.ui.model.ModuleStatus netShieldStatus, com.odin.core.ui.model.NetShieldUiState netShieldState, com.odin.core.ui.model.ModuleStatus vpnStatus, com.odin.core.ui.model.ModuleStatus permissionsStatus, boolean logsAvailable) {
        return null;
    }
    
    private final java.util.List<com.odin.core.ui.model.DashboardBlockedFeature> buildBlockedFeatures() {
        return null;
    }
    
    private final java.util.List<com.odin.core.ui.model.DashboardQuickAction> buildQuickActions() {
        return null;
    }
    
    private final java.lang.String label(com.odin.core.ui.model.ModuleStatus $this$label) {
        return null;
    }
    
    private final com.odin.core.ui.model.ModuleStatus toDashboardModuleStatus(com.odin.core.ui.model.SentryUiState $this$toDashboardModuleStatus) {
        return null;
    }
    
    private final com.odin.core.ui.model.ModuleStatus toDashboardModuleStatus(com.odin.core.ui.model.NetShieldUiState $this$toDashboardModuleStatus) {
        return null;
    }
    
    private final com.odin.core.ui.model.ModuleStatus toDashboardVpnStatus(com.odin.core.ui.model.NetShieldUiState $this$toDashboardVpnStatus) {
        return null;
    }
}