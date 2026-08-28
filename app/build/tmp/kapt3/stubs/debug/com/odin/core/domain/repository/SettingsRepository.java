package com.odin.core.domain.repository;

/**
 * SettingsRepository proporciona acceso a configuraciones.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H&J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H&J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u00a6@\u00a2\u0006\u0002\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/odin/core/domain/repository/SettingsRepository;", "", "getGeneralSettings", "Lkotlinx/coroutines/flow/Flow;", "Lcom/odin/core/domain/model/GeneralSettings;", "getNetworkSettings", "Lcom/odin/core/domain/model/NetworkSettings;", "getSecuritySettings", "Lcom/odin/core/domain/model/SecuritySettings;", "saveGeneralSettings", "", "settings", "(Lcom/odin/core/domain/model/GeneralSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveNetworkSettings", "(Lcom/odin/core/domain/model/NetworkSettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveSecuritySettings", "(Lcom/odin/core/domain/model/SecuritySettings;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface SettingsRepository {
    
    /**
     * Obtiene configuraciones generales.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.GeneralSettings> getGeneralSettings();
    
    /**
     * Guarda configuraciones generales.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveGeneralSettings(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.GeneralSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Obtiene configuraciones de seguridad.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.SecuritySettings> getSecuritySettings();
    
    /**
     * Guarda configuraciones de seguridad.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveSecuritySettings(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.SecuritySettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Obtiene configuraciones de red.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.odin.core.domain.model.NetworkSettings> getNetworkSettings();
    
    /**
     * Guarda configuraciones de red.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveNetworkSettings(@org.jetbrains.annotations.NotNull()
    com.odin.core.domain.model.NetworkSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}