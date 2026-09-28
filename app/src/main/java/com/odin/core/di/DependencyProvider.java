package com.odin.core.di;

import android.content.Context;

import com.odin.core.data.repository.FirewallRepositoryImpl;
import com.odin.core.domain.repository.FirewallRepository;

/**
 * DependencyProvider ofrece implementaciones desacopladas de los contratos del dominio.
 * Este patron permite un facil reemplazo de implementaciones locales por mocks o por
 * repositorios remotos en fases futuras.
 *
 * Phase 10: Los proveedores legacy de LogRepository y SystemStatusRepository
 * fueron eliminados. SentryService ahora usa SentryCore directamente para
 * persistencia cifrada canonica.
 */
public final class DependencyProvider {

    private DependencyProvider() {
        // Utility class
    }

    public static FirewallRepository provideFirewallRepository(Context context) {
        return new FirewallRepositoryImpl();
    }
}
