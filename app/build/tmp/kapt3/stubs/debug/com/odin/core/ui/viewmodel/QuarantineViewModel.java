package com.odin.core.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u000eJ\u000e\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u000eJ\u0010\u0010\u001e\u001a\u00020\u001a2\u0006\u0010\u001f\u001a\u00020 H\u0002J\b\u0010!\u001a\u00020\u001aH\u0002J\u0016\u0010\"\u001a\u00020\u001a2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010#\u001a\u00020 J\u000e\u0010$\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u000eJ\u000e\u0010%\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u000eR\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0019\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013\u00a8\u0006&"}, d2 = {"Lcom/odin/core/ui/viewmodel/QuarantineViewModel;", "Landroidx/lifecycle/ViewModel;", "quarantineRepository", "Lcom/odin/core/domain/repository/QuarantineRepository;", "context", "Landroid/content/Context;", "(Lcom/odin/core/domain/repository/QuarantineRepository;Landroid/content/Context;)V", "_behaviorData", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/odin/core/domain/model/BehaviorData;", "_isLoading", "", "_quarantinedApps", "", "Lcom/odin/core/domain/model/QuarantinedApp;", "_selectedApp", "behaviorData", "Lkotlinx/coroutines/flow/StateFlow;", "getBehaviorData", "()Lkotlinx/coroutines/flow/StateFlow;", "isLoading", "quarantinedApps", "getQuarantinedApps", "selectedApp", "getSelectedApp", "clearSelectedApp", "", "deleteApp", "app", "keepApp", "loadBehaviorData", "packageName", "", "loadQuarantinedApps", "quarantineApp", "reason", "releaseApp", "selectApp", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class QuarantineViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.domain.repository.QuarantineRepository quarantineRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.odin.core.domain.model.QuarantinedApp>> _quarantinedApps = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.odin.core.domain.model.QuarantinedApp>> quarantinedApps = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.odin.core.domain.model.QuarantinedApp> _selectedApp = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.odin.core.domain.model.QuarantinedApp> selectedApp = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.odin.core.domain.model.BehaviorData> _behaviorData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.odin.core.domain.model.BehaviorData> behaviorData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading = null;
    
    @javax.inject.Inject()
    public QuarantineViewModel(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.repository.QuarantineRepository quarantineRepository, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.odin.core.domain.model.QuarantinedApp>> getQuarantinedApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.odin.core.domain.model.QuarantinedApp> getSelectedApp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.odin.core.domain.model.BehaviorData> getBehaviorData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading() {
        return null;
    }
    
    private final void loadQuarantinedApps() {
    }
    
    public final void selectApp(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.QuarantinedApp app) {
    }
    
    public final void clearSelectedApp() {
    }
    
    private final void loadBehaviorData(java.lang.String packageName) {
    }
    
    public final void releaseApp(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.QuarantinedApp app) {
    }
    
    public final void deleteApp(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.QuarantinedApp app) {
    }
    
    public final void keepApp(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.QuarantinedApp app) {
    }
    
    public final void quarantineApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    java.lang.String reason) {
    }
}