package com.odin.core.domain.repository;

/**
 * FirewallRepository proporciona acceso a reglas de firewall.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH&J\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\r0\fH&J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H&J\u001e\u0010\u0013\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\u0010H\u00a6@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0017"}, d2 = {"Lcom/odin/core/domain/repository/FirewallRepository;", "", "createRule", "", "rule", "Lcom/odin/core/domain/model/FirewallRule;", "(Lcom/odin/core/domain/model/FirewallRule;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRule", "ruleId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveRules", "Lkotlinx/coroutines/flow/Flow;", "", "getAllRules", "isBlocked", "", "packet", "Lcom/odin/core/domain/model/NetworkPacket;", "toggleRule", "enabled", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateRule", "app_debug"})
public abstract interface FirewallRepository {
    
    /**
     * Obtiene todas las reglas.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.FirewallRule>> getAllRules();
    
    /**
     * Obtiene reglas activas.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.FirewallRule>> getActiveRules();
    
    /**
     * Crea una nueva regla.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Actualiza una regla.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateRule(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.FirewallRule rule, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Elimina una regla.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteRule(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Activa/desactiva una regla.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object toggleRule(@org.jetbrains.annotations.NotNull()
    java.lang.String ruleId, boolean enabled, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Evalúa si un paquete debe bloquearse según las reglas actuales.
     */
    public abstract boolean isBlocked(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.NetworkPacket packet);
}