package com.odin.core.ui.viewmodel;

import com.odin.core.domain.repository.ThreatsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ThreatsViewModel_Factory implements Factory<ThreatsViewModel> {
  private final Provider<ThreatsRepository> threatsRepositoryProvider;

  public ThreatsViewModel_Factory(Provider<ThreatsRepository> threatsRepositoryProvider) {
    this.threatsRepositoryProvider = threatsRepositoryProvider;
  }

  @Override
  public ThreatsViewModel get() {
    return newInstance(threatsRepositoryProvider.get());
  }

  public static ThreatsViewModel_Factory create(
      Provider<ThreatsRepository> threatsRepositoryProvider) {
    return new ThreatsViewModel_Factory(threatsRepositoryProvider);
  }

  public static ThreatsViewModel newInstance(ThreatsRepository threatsRepository) {
    return new ThreatsViewModel(threatsRepository);
  }
}
