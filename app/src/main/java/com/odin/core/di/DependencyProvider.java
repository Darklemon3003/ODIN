package com.odin.core.di;

import android.content.Context;

import com.odin.core.data.local.SecureLocalStorage;
import com.odin.core.data.repository.FirewallRepositoryImpl;
import com.odin.core.data.repository.LogRepositoryImpl;
import com.odin.core.data.repository.SystemStatusRepositoryImpl;
import com.odin.core.domain.repository.FirewallRepository;
import com.odin.core.domain.repository.LogRepository;
import com.odin.core.domain.repository.SystemStatusRepository;

/**
 * DependencyProvider ofrece implementaciones desacopladas de los contratos del dominio.
 * Este patrón permite un fácil reemplazo de implementaciones locales por mocks o por repositorios
 * remotos en fases futuras.
 */
public final class DependencyProvider {

    private DependencyProvider() {
        // Utility class
    }

    public static LogRepository provideLogRepository(Context context) {
        SecureLocalStorage storage = new SecureLocalStorage(context);
        return new LogRepositoryImpl(storage);
    }

    public static SystemStatusRepository provideSystemStatusRepository(Context context) {
        SecureLocalStorage storage = new SecureLocalStorage(context);
        return new SystemStatusRepositoryImpl(storage);
    }

    public static FirewallRepository provideFirewallRepository(Context context) {
        SecureLocalStorage storage = new SecureLocalStorage(context);
        return new FirewallRepositoryImpl(storage);
    }
}
