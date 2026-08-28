package com.odin.core.data.repository;

import com.odin.core.network.detection.ThreatDetectionEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ThreatsRepositoryIntegration_Factory implements Factory<ThreatsRepositoryIntegration> {
  private final Provider<ThreatDetectionEngine> threatDetectionEngineProvider;

  public ThreatsRepositoryIntegration_Factory(
      Provider<ThreatDetectionEngine> threatDetectionEngineProvider) {
    this.threatDetectionEngineProvider = threatDetectionEngineProvider;
  }

  @Override
  public ThreatsRepositoryIntegration get() {
    return newInstance(threatDetectionEngineProvider.get());
  }

  public static ThreatsRepositoryIntegration_Factory create(
      Provider<ThreatDetectionEngine> threatDetectionEngineProvider) {
    return new ThreatsRepositoryIntegration_Factory(threatDetectionEngineProvider);
  }

  public static ThreatsRepositoryIntegration newInstance(
      ThreatDetectionEngine threatDetectionEngine) {
    return new ThreatsRepositoryIntegration(threatDetectionEngine);
  }
}
