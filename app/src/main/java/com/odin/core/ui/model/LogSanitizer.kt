package com.odin.core.ui.model

import com.odin.core.domain.model.LogEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LogSanitizer {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val urlPattern = Regex("""(?i)\bhttps?://[^\s]+""")
    private val tokenPattern = Regex("""(?i)\b(token|api[_-]?key|secret|password|authorization|bearer)\s*[:=]\s*["']?[^"',\s]+""")
    private val payloadPattern = Regex("""(?i)\b(payload|body|raw|content)\s*[:=]\s*.+""")
    private val androidPathPattern = Regex("""/(storage|sdcard|data)/[^\s,;]+""")
    private val windowsPathPattern = Regex("""[A-Za-z]:\\[^\s,;]+""")
    private val emailPattern = Regex("""\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}\b""")
    private val ipv4Pattern = Regex("""\b(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\b""")

    fun toUi(event: LogEvent): LogEventUi {
        val module = mapModule(event.source)
        val severity = mapSeverity(event.severity)
        val safeMessage = sanitize(event.message)
        val safeMetadata = sanitizeMetadata(event.metadata)
        val details = buildString {
            append(safeMessage)
            if (safeMetadata.isNotBlank()) {
                append("\n\nDetalles sanitizados:\n")
                append(safeMetadata)
            }
        }.trim()

        return LogEventUi(
            id = "${event.timestamp}-${event.source.hashCode()}-${event.message.hashCode()}",
            timestamp = event.timestamp,
            formattedTime = dateFormat.format(Date(event.timestamp)),
            module = module,
            severity = severity,
            title = titleFor(event.source, module),
            summary = safeMessage.ifBlank { "Evento local sin descripcion." }.limit(140),
            sanitizedDetails = details.ifBlank { "Sin detalles adicionales." },
            source = sanitizeSource(event.source),
            hasDetails = details.isNotBlank(),
            canExport = false
        )
    }

    fun sanitize(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return raw
            .replace(urlPattern, "[url-redacted]")
            .replace(tokenPattern) { match -> "${match.groupValues[1]}=[redacted]" }
            .replace(payloadPattern) { match -> "${match.groupValues[1]}=[redacted]" }
            .replace(emailPattern, "[email-redacted]")
            .replace(androidPathPattern) { match -> maskPath(match.value, '/') }
            .replace(windowsPathPattern) { match -> maskPath(match.value, '\\') }
            .replace(ipv4Pattern) { match -> maskIp(match.groupValues) }
    }

    private fun sanitizeMetadata(metadata: Map<String, String>): String {
        if (metadata.isEmpty()) return ""
        return metadata.entries
            .sortedBy { it.key }
            .joinToString("\n") { (key, value) ->
                val safeKey = sanitizeKey(key)
                val safeValue = if (isSensitiveKey(key)) "[redacted]" else sanitize(value).limit(180)
                "$safeKey: $safeValue"
            }
    }

    private fun sanitizeKey(key: String): String {
        return key.replace(Regex("""[^A-Za-z0-9_.-]"""), "_").limit(48)
    }

    private fun isSensitiveKey(key: String): Boolean {
        val normalized = key.lowercase(Locale.US)
        return listOf("token", "secret", "password", "payload", "raw", "authorization", "cookie")
            .any { normalized.contains(it) }
    }

    private fun maskIp(groups: List<String>): String {
        val first = groups[1].toIntOrNull() ?: return "x.x.x.x"
        val second = groups[2].toIntOrNull() ?: 0
        return when {
            first == 10 -> "10.x.x.x"
            first == 192 && second == 168 -> "192.168.x.x"
            first == 172 && second in 16..31 -> "172.x.x.x"
            else -> "${groups[1]}.${groups[2]}.x.x"
        }
    }

    private fun maskPath(path: String, separator: Char): String {
        val normalized = path.trim()
        val fileName = normalized.substringAfterLast(separator, missingDelimiterValue = "archivo")
        return if (separator == '/') {
            "/storage/.../$fileName"
        } else {
            "C:\\...\\$fileName"
        }
    }

    private fun titleFor(source: String, module: LogModule): String {
        val sourceTitle = source
            .replace('_', ' ')
            .lowercase(Locale.getDefault())
            .replaceFirstChar { it.titlecase(Locale.getDefault()) }
        return sourceTitle.ifBlank { module.label }
    }

    private fun sanitizeSource(source: String): String {
        return source.replace(Regex("""[^A-Za-z0-9_.-]"""), "_").limit(64)
    }

    private fun mapModule(source: String): LogModule {
        val normalized = source.uppercase(Locale.US)
        return when {
            normalized.contains("SENTRY_BOOT") || normalized.contains("BOOT") -> LogModule.BOOT
            normalized.contains("SENTRY") || normalized.contains("STATUS_PUBLISH") -> LogModule.SENTRY
            normalized.contains("NETSHIELD") || normalized.contains("VPN") || normalized.contains("THREAT_DETECTION") -> LogModule.NETSHIELD
            normalized.contains("PERMISSION") || normalized.contains("NOTIFICATION") -> LogModule.PERMISSIONS
            normalized.contains("FIREWALL") || normalized.contains("PACKET_BLOCKED") -> LogModule.FIREWALL
            normalized.contains("QUARANTINE") -> LogModule.QUARANTINE
            normalized.contains("UI") -> LogModule.UI
            normalized.contains("CRASH") || normalized.contains("AUTO_HEALING") || normalized.contains("LOG_ROTATION") || normalized.contains("CORE") -> LogModule.SYSTEM
            else -> LogModule.UNKNOWN
        }
    }

    private fun mapSeverity(severity: LogEvent.Severity): LogSeverity {
        return when (severity) {
            LogEvent.Severity.INFO -> LogSeverity.INFO
            LogEvent.Severity.WARNING -> LogSeverity.WARNING
            LogEvent.Severity.ERROR -> LogSeverity.ERROR
            LogEvent.Severity.CRITICAL -> LogSeverity.CRITICAL
        }
    }

    private fun String.limit(maxLength: Int): String {
        return if (length <= maxLength) this else take(maxLength - 1).trimEnd() + "..."
    }
}
