package com.odin.core.di;

import android.content.Context;
import com.odin.core.forensics.LogExporter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class IntegrationModule_ProvideLogExporterFactory implements Factory<LogExporter> {
  private final Provider<Context> contextProvider;

  public IntegrationModule_ProvideLogExporterFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LogExporter get() {
    return provideLogExporter(contextProvider.get());
  }

  public static IntegrationModule_ProvideLogExporterFactory create(
      Provider<Context> contextProvider) {
    return new IntegrationModule_ProvideLogExporterFactory(contextProvider);
  }

  public static LogExporter provideLogExporter(Context context) {
    return Preconditions.checkNotNullFromProvides(IntegrationModule.INSTANCE.provideLogExporter(context));
  }
}
