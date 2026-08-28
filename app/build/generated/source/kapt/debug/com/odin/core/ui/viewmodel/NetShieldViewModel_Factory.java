package com.odin.core.ui.viewmodel;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class NetShieldViewModel_Factory implements Factory<NetShieldViewModel> {
  private final Provider<Context> contextProvider;

  public NetShieldViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NetShieldViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static NetShieldViewModel_Factory create(Provider<Context> contextProvider) {
    return new NetShieldViewModel_Factory(contextProvider);
  }

  public static NetShieldViewModel newInstance(Context context) {
    return new NetShieldViewModel(context);
  }
}
