package com.odin.core.domain.repository;

/**
 * SecurityRepository proporciona acceso a datos de seguridad.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H&J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/odin/core/domain/repository/SecurityRepository;", "", "getMetrics", "Lkotlinx/coroutines/flow/Flow;", "Lcom/odin/core/domain/model/SecurityMetrics;", "getSecurityStatus", "Lcom/odin/core/domain/model/SecurityStatus;", "updateSecurityStatus", "", "status", "(Lcom/odin/core/domain/model/SecurityStatus;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface SecurityRepository {
    
    /**
     * Obtiene el estado de seguridad actual.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.SecurityStatus> getSecurityStatus();
    
    /**
     * Obtiene las métricas de seguridad.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.SecurityMetrics> getMetrics();
    
    /**
     * Actualiza el estado de seguridad.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSecurityStatus(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.SecurityStatus status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}