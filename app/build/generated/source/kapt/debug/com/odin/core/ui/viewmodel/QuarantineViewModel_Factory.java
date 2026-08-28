package com.odin.core.ui.viewmodel;

import android.content.Context;
import com.odin.core.domain.repository.QuarantineRepository;
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
public final class QuarantineViewModel_Factory implements Factory<QuarantineViewModel> {
  private final Provider<QuarantineRepository> quarantineRepositoryProvider;

  private final Provider<Context> contextProvider;

  public QuarantineViewModel_Factory(Provider<QuarantineRepository> quarantineRepositoryProvider,
      Provider<Context> contextProvider) {
    this.quarantineRepositoryProvider = quarantineRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public QuarantineViewModel get() {
    return newInstance(quarantineRepositoryProvider.get(), contextProvider.get());
  }

  public static QuarantineViewModel_Factory create(
      Provider<QuarantineRepository> quarantineRepositoryProvider,
      Provider<Context> contextProvider) {
    return new QuarantineViewModel_Factory(quarantineRepositoryProvider, contextProvider);
  }

  public static QuarantineViewModel newInstance(QuarantineRepository quarantineRepository,
      Context context) {
    return new QuarantineViewModel(quarantineRepository, context);
  }
}
