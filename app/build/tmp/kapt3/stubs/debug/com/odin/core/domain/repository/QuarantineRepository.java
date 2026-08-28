package com.odin.core.domain.repository;

/**
 * QuarantineRepository proporciona acceso a aplicaciones en cuarentena.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bH&J\u0016\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\b2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0012"}, d2 = {"Lcom/odin/core/domain/repository/QuarantineRepository;", "", "deleteApp", "", "packageName", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllQuarantinedApps", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/odin/core/domain/model/QuarantinedApp;", "getBehaviorData", "Lcom/odin/core/domain/model/BehaviorData;", "keepApp", "quarantineApp", "reason", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "releaseApp", "app_debug"})
public abstract interface QuarantineRepository {
    
    /**
     * Obtiene todas las aplicaciones en cuarentena.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.odin.core.domain.model.QuarantinedApp>> getAllQuarantinedApps();
    
    /**
     * Obtiene datos de comportamiento de una aplicación.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.BehaviorData> getBehaviorData(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName);
    
    /**
     * Libera una aplicación de cuarentena.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object releaseApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Elimina una aplicación.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Mantiene una aplicación en cuarentena.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object keepApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Pone una aplicación en cuarentena.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object quarantineApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    java.lang.String reason, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}