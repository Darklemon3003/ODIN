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
public final class FirewallRepositoryImpl_Factory implements Factory<FirewallRepositoryImpl> {
  @Override
  public FirewallRepositoryImpl get() {
    return newInstance();
  }

  public static FirewallRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FirewallRepositoryImpl newInstance() {
    return new FirewallRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final FirewallRepositoryImpl_Factory INSTANCE = new FirewallRepositoryImpl_Factory();
  }
}
