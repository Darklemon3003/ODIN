package com.odin.core.di

import android.content.Context
import com.odin.core.data.repository.LogsRepositoryImpl
import com.odin.core.domain.repository.LogsRepository
import com.odin.core.forensics.LogExporter
import com.odin.core.network.detection.ThreatDetectionEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IntegrationModule {
    
    @Provides
    @Singleton
    fun provideThreatDetectionEngine(): ThreatDetectionEngine {
        return ThreatDetectionEngine()
    }
    
    @Provides
    @Singleton
    fun provideLogExporter(@ApplicationContext context: Context): LogExporter {
        return LogExporter.getInstance(context)
    }
}
