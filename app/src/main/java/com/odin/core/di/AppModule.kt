package com.odin.core.di

import com.odin.core.data.repository.FirewallRepositoryImpl
import com.odin.core.data.repository.LogsRepositoryIntegration
import com.odin.core.data.repository.QuarantineRepositoryImpl
import com.odin.core.data.repository.SecurityRepositoryImpl
import com.odin.core.data.repository.SettingsRepositoryImpl
import com.odin.core.data.repository.ThreatsRepositoryIntegration
import com.odin.core.domain.repository.FirewallRepository
import com.odin.core.domain.repository.LogsRepository
import com.odin.core.domain.repository.QuarantineRepository
import com.odin.core.domain.repository.SecurityRepository
import com.odin.core.domain.repository.SettingsRepository
import com.odin.core.domain.repository.ThreatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    @Binds
    @Singleton
    abstract fun bindSecurityRepository(
        impl: SecurityRepositoryImpl
    ): SecurityRepository
    
    @Binds
    @Singleton
    abstract fun bindLogsRepository(
        impl: LogsRepositoryIntegration
    ): LogsRepository
    
    @Binds
    @Singleton
    abstract fun bindThreatsRepository(
        impl: ThreatsRepositoryIntegration
    ): ThreatsRepository
    
    @Binds
    @Singleton
    abstract fun bindFirewallRepository(
        impl: FirewallRepositoryImpl
    ): FirewallRepository
    
    @Binds
    @Singleton
    abstract fun bindQuarantineRepository(
        impl: QuarantineRepositoryImpl
    ): QuarantineRepository
    
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
