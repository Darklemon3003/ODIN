package com.odin.core

import android.app.Application
import com.odin.core.persistence.LegacyPreferenceMigration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OdinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Migrar preferencias legacy plaintext a almacenamiento cifrado canonico.
        // Idempotente: seguro de ejecutar en cada arranque.
        try {
            LegacyPreferenceMigration.migrateIfNeeded(this)
        } catch (e: Exception) {
            // La migracion fallida no debe bloquear el arranque de la app.
            // Se reintentara en el siguiente arranque.
            android.util.Log.w("OdinApplication", "Migracion legacy pendiente: ${e.message}")
        }
    }
}
