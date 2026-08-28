package com.odin.core.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0012J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0014H\u0016J\u0016\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\t0\u00142\u0006\u0010\u0011\u001a\u00020\bH\u0016J\u0016\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0012J\b\u0010\u0017\u001a\u00020\u0010H\u0002J\u001e\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\u0006\u0010\u0011\u001a\u00020\bH\u0002J\u0006\u0010\u001c\u001a\u00020\u0010J\u0016\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\u0012J\f\u0010\u001e\u001a\u00020\f*\u00020\u001fH\u0002J\f\u0010 \u001a\u00020!*\u00020\"H\u0002R \u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/odin/core/data/repository/QuarantineRepositoryImpl;", "Lcom/odin/core/domain/repository/QuarantineRepository;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_behaviorData", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "Lcom/odin/core/domain/model/BehaviorData;", "_quarantinedApps", "", "Lcom/odin/core/domain/model/QuarantinedApp;", "quarantineManager", "Lcom/odin/core/network/response/QuarantineManager;", "deleteApp", "", "packageName", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllQuarantinedApps", "Lkotlinx/coroutines/flow/Flow;", "getBehaviorData", "keepApp", "loadRealQuarantinedApps", "quarantineApp", "reason", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "readRequestedPermissions", "refreshQuarantinedApps", "releaseApp", "toDomainModel", "Lcom/odin/core/network/response/QuarantineManager$QuarantineRecord;", "toRiskScore", "", "Lcom/odin/core/network/response/QuarantineManager$QuarantineSeverity;", "app_debug"})
public final class QuarantineRepositoryImpl implements com.odin.core.domain.repository.QuarantineRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.network.response.QuarantineManager quarantineManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.odin.core.domain.model.QuarantinedApp>> _quarantinedApps = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, com.odin.core.domain.model.BehaviorData>> _behaviorData = null;
    
    @javax.inject.Inject()
    public QuarantineRepositoryImpl(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final void loadRealQuarantinedApps() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.QuarantinedApp>> getAllQuarantinedApps() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.BehaviorData> getBehaviorData(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object releaseApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object keepApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object quarantineApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    java.lang.String reason, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void refreshQuarantinedApps() {
    }
    
    private final com.odin.core.domain.model.QuarantinedApp toDomainModel(com.odin.core.network.response.QuarantineManager.QuarantineRecord $this$toDomainModel) {
        return null;
    }
    
    private final int toRiskScore(com.odin.core.network.response.QuarantineManager.QuarantineSeverity $this$toRiskScore) {
        return 0;
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    private final java.util.List<java.lang.String> readRequestedPermissions(java.lang.String packageName) {
        return null;
    }
}