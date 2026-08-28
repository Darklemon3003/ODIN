package com.odin.core.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0005J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0013J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0015H\u0016J\u0014\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0015H\u0016J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\rH\u0002J\u0006\u0010\u001c\u001a\u00020\rJ\u001e\u0010\u001d\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010 \u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tH\u0096@\u00a2\u0006\u0002\u0010\u000fR\u001a\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/odin/core/data/repository/FirewallRepositoryImpl;", "Lcom/odin/core/domain/repository/FirewallRepository;", "storage", "Lcom/odin/core/data/local/SecureLocalStorage;", "(Lcom/odin/core/data/local/SecureLocalStorage;)V", "()V", "_rules", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/odin/core/domain/model/FirewallRule;", "dynamicRuleEngine", "Lcom/odin/core/network/rules/DynamicRuleEngine;", "createRule", "", "rule", "(Lcom/odin/core/domain/model/FirewallRule;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRule", "ruleId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveRules", "Lkotlinx/coroutines/flow/Flow;", "getAllRules", "isBlocked", "", "packet", "Lcom/odin/core/domain/model/NetworkPacket;", "loadRealRules", "refreshRules", "toggleRule", "enabled", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateRule", "app_debug"})
public final class FirewallRepositoryImpl implements com.odin.core.domain.repository.FirewallRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.odin.core.network.rules.DynamicRuleEngine dynamicRuleEngine = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.odin.core.domain.model.FirewallRule>> _rules = null;
    
    @javax.inject.Inject()
    public FirewallRepositoryImpl() {
        super();
    }
    
    public FirewallRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.odin.core.data.local.SecureLocalStorage storage) {
        super();
    }
    
    private final void loadRealRules() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.FirewallRule>> getAllRules() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.FirewallRule>> getActiveRules() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object createRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteRule(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object toggleRule(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleId, boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public boolean isBlocked(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.NetworkPacket packet) {
        return false;
    }
    
    public final void refreshRules() {
    }
}