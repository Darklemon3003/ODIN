package com.odin.core.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ThreatsRepositoryImpl_Factory implements Factory<ThreatsRepositoryImpl> {
  @Override
  public ThreatsRepositoryImpl get() {
    return newInstance();
  }

  public static ThreatsRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ThreatsRepositoryImpl newInstance() {
    return new ThreatsRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final ThreatsRepositoryImpl_Factory INSTANCE = new ThreatsRepositoryImpl_Factory();
  }
}
