package com.odin.core.ui.viewmodel;

import android.content.Context;
import com.odin.core.domain.repository.LogsRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<LogsRepository> logsRepositoryProvider;

  public DashboardViewModel_Factory(Provider<Context> contextProvider,
      Provider<LogsRepository> logsRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.logsRepositoryProvider = logsRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(contextProvider.get(), logsRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<Context> contextProvider,
      Provider<LogsRepository> logsRepositoryProvider) {
    return new DashboardViewModel_Factory(contextProvider, logsRepositoryProvider);
  }

  public static DashboardViewModel newInstance(Context context, LogsRepository logsRepository) {
    return new DashboardViewModel(context, logsRepository);
  }
}
