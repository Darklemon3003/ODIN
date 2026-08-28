package com.odin.core.ui.viewmodel;

import com.odin.core.domain.repository.FirewallRepository;
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
public final class FirewallViewModel_Factory implements Factory<FirewallViewModel> {
  private final Provider<FirewallRepository> firewallRepositoryProvider;

  public FirewallViewModel_Factory(Provider<FirewallRepository> firewallRepositoryProvider) {
    this.firewallRepositoryProvider = firewallRepositoryProvider;
  }

  @Override
  public FirewallViewModel get() {
    return newInstance(firewallRepositoryProvider.get());
  }

  public static FirewallViewModel_Factory create(
      Provider<FirewallRepository> firewallRepositoryProvider) {
    return new FirewallViewModel_Factory(firewallRepositoryProvider);
  }

  public static FirewallViewModel newInstance(FirewallRepository firewallRepository) {
    return new FirewallViewModel(firewallRepository);
  }
}
