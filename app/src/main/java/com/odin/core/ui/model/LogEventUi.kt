package com.odin.core.ui.model

data class LogEventUi(
    val id: String,
    val timestamp: Long,
    val formattedTime: String,
    val module: LogModule,
    val severity: LogSeverity,
    val title: String,
    val summary: String,
    val sanitizedDetails: String,
    val source: String,
    val hasDetails: Boolean,
    val canExport: Boolean = false
)

enum class LogModule(val label: String) {
    SENTRY("Sentry"),
    NETSHIELD("NetShield"),
    BOOT("Boot"),
    PERMISSIONS("Permisos"),
    FIREWALL("Firewall"),
    QUARANTINE("Cuarentena"),
    SYSTEM("Sistema"),
    UI("UI"),
    UNKNOWN("Desconocido")
}

enum class LogSeverity(val label: String) {
    INFO("Info"),
    WARNING("Advertencia"),
    ERROR("Error"),
    CRITICAL("Critico")
}
