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
public final class LogsViewModel_Factory implements Factory<LogsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<LogsRepository> logsRepositoryProvider;

  public LogsViewModel_Factory(Provider<Context> contextProvider,
      Provider<LogsRepository> logsRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.logsRepositoryProvider = logsRepositoryProvider;
  }

  @Override
  public LogsViewModel get() {
    return newInstance(contextProvider.get(), logsRepositoryProvider.get());
  }

  public static LogsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<LogsRepository> logsRepositoryProvider) {
    return new LogsViewModel_Factory(contextProvider, logsRepositoryProvider);
  }

  public static LogsViewModel newInstance(Context context, LogsRepository logsRepository) {
    return new LogsViewModel(context, logsRepository);
  }
}
