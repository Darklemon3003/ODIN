package com.odin.core.netshield

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Properties

internal class NetShieldRuntimeStore(
    private val stateFile: File,
    private val clock: () -> Long = System::currentTimeMillis
) {
    sealed interface ReadResult {
        data class Available(val snapshot: NetShieldRuntimeState.Snapshot) : ReadResult
        data object Missing : ReadResult
        data object Unavailable : ReadResult
    }

    private val lockFile = File(stateFile.parentFile, "${stateFile.name}.lock")

    fun read(): ReadResult = locked {
        if (!stateFile.isFile) {
            ReadResult.Missing
        } else {
            runCatching { ReadResult.Available(readUnlocked()) }
                .getOrDefault(ReadResult.Unavailable)
        }
    } ?: ReadResult.Unavailable

    fun initialize(snapshot: NetShieldRuntimeState.Snapshot): Boolean = locked {
        if (stateFile.isFile) true else writeUnlocked(snapshot)
    } ?: false

    fun markVpnAuthorized(authorized: Boolean): Boolean = update {
        it.copy(vpnAuthorized = authorized, lastError = null)
    }

    fun markConnectivityRiskAccepted(accepted: Boolean): Boolean = update {
        it.copy(connectivityRiskAccepted = accepted)
    }

    fun markStarting(message: String?): Boolean = update {
        it.copy(
            state = NetShieldRuntimeState.STATE_STARTING,
            serviceRunning = false,
            tunnelEstablished = false,
            lastUpdated = clock(),
            lastError = message
        )
    }

    fun markRunning(): Boolean = update {
        val now = clock()
        it.copy(
            state = NetShieldRuntimeState.STATE_RUNNING,
            serviceRunning = true,
            tunnelEstablished = true,
            lastHeartbeat = now,
            lastStarted = now,
            lastUpdated = now,
            lastError = null
        )
    }

    fun markHeartbeat(): Boolean = update {
        val now = clock()
        it.copy(
            state = NetShieldRuntimeState.STATE_RUNNING,
            serviceRunning = true,
            tunnelEstablished = true,
            lastHeartbeat = now,
            lastUpdated = now
        )
    }

    fun markPacketObserved(bytes: Int, allowed: Boolean): Boolean = update {
        val now = clock()
        it.copy(
            state = NetShieldRuntimeState.STATE_RUNNING,
            serviceRunning = true,
            tunnelEstablished = true,
            lastHeartbeat = now,
            lastUpdated = now,
            lastPacketTime = now,
            packetsAudited = it.packetsAudited + 1L,
            packetsAllowed = it.packetsAllowed + if (allowed) 1L else 0L,
            packetsBlocked = it.packetsBlocked + if (allowed) 0L else 1L,
            bytesIn = it.bytesIn + bytes.toLong()
        )
    }

    fun markStopping(): Boolean = update {
        it.copy(
            state = NetShieldRuntimeState.STATE_STOPPING,
            serviceRunning = false,
            tunnelEstablished = false,
            lastUpdated = clock()
        )
    }

    fun markStopped(message: String?): Boolean = update {
        val now = clock()
        it.copy(
            state = NetShieldRuntimeState.STATE_STOPPED,
            serviceRunning = false,
            tunnelEstablished = false,
            lastStopped = now,
            lastUpdated = now,
            lastError = message
        )
    }

    fun markError(message: String?): Boolean = update {
        it.copy(
            state = NetShieldRuntimeState.STATE_ERROR,
            serviceRunning = false,
            tunnelEstablished = false,
            lastUpdated = clock(),
            lastError = message
        )
    }

    private fun update(transform: (NetShieldRuntimeState.Snapshot) -> NetShieldRuntimeState.Snapshot): Boolean =
        locked {
            val current = if (stateFile.isFile) {
                runCatching { readUnlocked() }.getOrElse { return@locked false }
            } else {
                NetShieldRuntimeState.unknownSnapshot()
            }
            writeUnlocked(transform(current).copy(persistenceAvailable = true))
        } ?: false

    private fun readUnlocked(): NetShieldRuntimeState.Snapshot {
        val properties = Properties()
        FileInputStream(stateFile).use(properties::load)
        return NetShieldRuntimeState.Snapshot(
            state = properties.getProperty(KEY_STATE, NetShieldRuntimeState.STATE_UNKNOWN),
            serviceRunning = properties.boolean(KEY_SERVICE_RUNNING),
            tunnelEstablished = properties.boolean(KEY_TUNNEL_ESTABLISHED),
            vpnAuthorized = properties.boolean(KEY_VPN_AUTHORIZED),
            connectivityRiskAccepted = properties.boolean(KEY_CONNECTIVITY_RISK_ACCEPTED),
            lastHeartbeat = properties.long(KEY_LAST_HEARTBEAT),
            lastStarted = properties.long(KEY_LAST_STARTED),
            lastStopped = properties.long(KEY_LAST_STOPPED),
            lastUpdated = properties.long(KEY_LAST_UPDATED),
            lastError = properties.getProperty(KEY_LAST_ERROR)?.takeIf(String::isNotBlank),
            packetsAudited = properties.long(KEY_PACKETS_AUDITED),
            packetsAllowed = properties.long(KEY_PACKETS_ALLOWED),
            packetsBlocked = properties.long(KEY_PACKETS_BLOCKED),
            bytesIn = properties.long(KEY_BYTES_IN),
            bytesOut = properties.long(KEY_BYTES_OUT),
            lastPacketTime = properties.long(KEY_LAST_PACKET_TIME),
            persistenceAvailable = true
        )
    }

    private fun writeUnlocked(snapshot: NetShieldRuntimeState.Snapshot): Boolean {
        val parent = stateFile.parentFile ?: return false
        if (!parent.exists() && !parent.mkdirs()) return false
        val properties = Properties().apply {
            setProperty(KEY_STATE, snapshot.state)
            setProperty(KEY_SERVICE_RUNNING, snapshot.serviceRunning.toString())
            setProperty(KEY_TUNNEL_ESTABLISHED, snapshot.tunnelEstablished.toString())
            setProperty(KEY_VPN_AUTHORIZED, snapshot.vpnAuthorized.toString())
            setProperty(KEY_CONNECTIVITY_RISK_ACCEPTED, snapshot.connectivityRiskAccepted.toString())
            setProperty(KEY_LAST_HEARTBEAT, snapshot.lastHeartbeat.toString())
            setProperty(KEY_LAST_STARTED, snapshot.lastStarted.toString())
            setProperty(KEY_LAST_STOPPED, snapshot.lastStopped.toString())
            setProperty(KEY_LAST_UPDATED, snapshot.lastUpdated.toString())
            snapshot.lastError?.let { setProperty(KEY_LAST_ERROR, it) }
            setProperty(KEY_PACKETS_AUDITED, snapshot.packetsAudited.toString())
            setProperty(KEY_PACKETS_ALLOWED, snapshot.packetsAllowed.toString())
            setProperty(KEY_PACKETS_BLOCKED, snapshot.packetsBlocked.toString())
            setProperty(KEY_BYTES_IN, snapshot.bytesIn.toString())
            setProperty(KEY_BYTES_OUT, snapshot.bytesOut.toString())
            setProperty(KEY_LAST_PACKET_TIME, snapshot.lastPacketTime.toString())
        }
        var tempFile: File? = null
        return try {
            val pendingFile = File.createTempFile(stateFile.name, ".tmp", parent)
            tempFile = pendingFile
            FileOutputStream(pendingFile).use { output ->
                properties.store(output, null)
                output.fd.sync()
            }
            try {
                Files.move(
                    pendingFile.toPath(),
                    stateFile.toPath(),
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
                )
            } catch (_: AtomicMoveNotSupportedException) {
                Files.move(pendingFile.toPath(), stateFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            true
        } catch (_: Exception) {
            false
        } finally {
            tempFile?.delete()
        }
    }

    private fun <T> locked(block: () -> T): T? = synchronized(processLock) {
        val parent = lockFile.parentFile ?: return@synchronized null
        if (!parent.exists() && !parent.mkdirs()) return@synchronized null
        try {
            RandomAccessFile(lockFile, "rw").use { lockAccess ->
                lockAccess.channel.use { channel ->
                    val lock = channel.lock()
                    try {
                        block()
                    } finally {
                        lock.release()
                    }
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun Properties.boolean(key: String): Boolean =
        getProperty(key)?.toBooleanStrictOrNull() ?: false

    private fun Properties.long(key: String): Long = getProperty(key)?.toLongOrNull() ?: 0L

    private companion object {
        val processLock = Any()

        const val KEY_STATE = "state"
        const val KEY_SERVICE_RUNNING = "service_running"
        const val KEY_TUNNEL_ESTABLISHED = "tunnel_established"
        const val KEY_VPN_AUTHORIZED = "vpn_authorized"
        const val KEY_CONNECTIVITY_RISK_ACCEPTED = "connectivity_risk_accepted"
        const val KEY_LAST_HEARTBEAT = "last_heartbeat"
        const val KEY_LAST_STARTED = "last_started"
        const val KEY_LAST_STOPPED = "last_stopped"
        const val KEY_LAST_UPDATED = "last_updated"
        const val KEY_LAST_ERROR = "last_error"
        const val KEY_PACKETS_AUDITED = "packets_audited"
        const val KEY_PACKETS_ALLOWED = "packets_allowed"
        const val KEY_PACKETS_BLOCKED = "packets_blocked"
        const val KEY_BYTES_IN = "bytes_in"
        const val KEY_BYTES_OUT = "bytes_out"
        const val KEY_LAST_PACKET_TIME = "last_packet_time"
    }
}
