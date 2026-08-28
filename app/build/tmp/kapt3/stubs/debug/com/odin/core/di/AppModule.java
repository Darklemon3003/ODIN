package com.odin.core.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\fH\'J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000fH\'J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0012H\'J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0015H\'\u00a8\u0006\u0016"}, d2 = {"Lcom/odin/core/di/AppModule;", "", "()V", "bindFirewallRepository", "Lcom/odin/core/domain/repository/FirewallRepository;", "impl", "Lcom/odin/core/data/repository/FirewallRepositoryImpl;", "bindLogsRepository", "Lcom/odin/core/domain/repository/LogsRepository;", "Lcom/odin/core/data/repository/LogsRepositoryIntegration;", "bindQuarantineRepository", "Lcom/odin/core/domain/repository/QuarantineRepository;", "Lcom/odin/core/data/repository/QuarantineRepositoryImpl;", "bindSecurityRepository", "Lcom/odin/core/domain/repository/SecurityRepository;", "Lcom/odin/core/data/repository/SecurityRepositoryImpl;", "bindSettingsRepository", "Lcom/odin/core/domain/repository/SettingsRepository;", "Lcom/odin/core/data/repository/SettingsRepositoryImpl;", "bindThreatsRepository", "Lcom/odin/core/domain/repository/ThreatsRepository;", "Lcom/odin/core/data/repository/ThreatsRepositoryIntegration;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class AppModule {
    
    public AppModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.odin.core.domain.repository.SecurityRepository bindSecurityRepository(@org.jetbrains.annotations.NotNull()
    com.odin.core.data.repository.SecurityRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.odin.core.domain.repository.LogsRepository bindLogsRepository(@org.jetbrains.annotations.NotNull()
    com.odin.core.data.repository.LogsRepositoryIntegration impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.odin.core.domain.repository.ThreatsRepository bindThreatsRepository(@org.jetbrains.annotations.NotNull()
    com.odin.core.data.repository.ThreatsRepositoryIntegration impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.odin.core.domain.repository.FirewallRepository bindFirewallRepository(@org.jetbrains.annotations.NotNull()
    com.odin.core.data.repository.FirewallRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.odin.core.domain.repository.QuarantineRepository bindQuarantineRepository(@org.jetbrains.annotations.NotNull()
    com.odin.core.data.repository.QuarantineRepositoryImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.odin.core.domain.repository.SettingsRepository bindSettingsRepository(@org.jetbrains.annotations.NotNull()
    com.odin.core.data.repository.SettingsRepositoryImpl impl);
}