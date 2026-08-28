package com.odin.core.di;

import com.odin.core.network.detection.ThreatDetectionEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class IntegrationModule_ProvideThreatDetectionEngineFactory implements Factory<ThreatDetectionEngine> {
  @Override
  public ThreatDetectionEngine get() {
    return provideThreatDetectionEngine();
  }

  public static IntegrationModule_ProvideThreatDetectionEngineFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ThreatDetectionEngine provideThreatDetectionEngine() {
    return Preconditions.checkNotNullFromProvides(IntegrationModule.INSTANCE.provideThreatDetectionEngine());
  }

  private static final class InstanceHolder {
    private static final IntegrationModule_ProvideThreatDetectionEngineFactory INSTANCE = new IntegrationModule_ProvideThreatDetectionEngineFactory();
  }
}
