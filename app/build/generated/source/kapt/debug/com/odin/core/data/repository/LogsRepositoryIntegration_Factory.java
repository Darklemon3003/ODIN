package com.odin.core.data.repository;

import android.content.Context;
import com.odin.core.forensics.LogExporter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class LogsRepositoryIntegration_Factory implements Factory<LogsRepositoryIntegration> {
  private final Provider<Context> contextProvider;

  private final Provider<LogExporter> logExporterProvider;

  public LogsRepositoryIntegration_Factory(Provider<Context> contextProvider,
      Provider<LogExporter> logExporterProvider) {
    this.contextProvider = contextProvider;
    this.logExporterProvider = logExporterProvider;
  }

  @Override
  public LogsRepositoryIntegration get() {
    return newInstance(contextProvider.get(), logExporterProvider.get());
  }

  public static LogsRepositoryIntegration_Factory create(Provider<Context> contextProvider,
      Provider<LogExporter> logExporterProvider) {
    return new LogsRepositoryIntegration_Factory(contextProvider, logExporterProvider);
  }

  public static LogsRepositoryIntegration newInstance(Context context, LogExporter logExporter) {
    return new LogsRepositoryIntegration(context, logExporter);
  }
}
