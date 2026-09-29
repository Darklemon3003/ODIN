package com.odin.core.netshield

import android.content.Context
import com.odin.core.ui.model.NetShieldRuntimeStatus

object NetShieldRuntimeState {
    private const val LEGACY_PREFS_NAME = "odin_netshield_runtime"
    private const val KEY_STORE_INITIALIZED = "cross_process_store_initialized"
    private const val STATE_FILE_NAME = "netshield_runtime_state.properties"
    private const val HEARTBEAT_TIMEOUT_MS = 90_000L

    const val STATE_UNKNOWN = "UNKNOWN"
    const val STATE_STOPPED = "STOPPED"
    const val STATE_STARTING = "STARTING"
    const val STATE_RUNNING = "RUNNING"
    const val STATE_STOPPING = "STOPPING"
    const val STATE_ERROR = "ERROR"

    data class Snapshot(
        val state: String,
        val serviceRunning: Boolean,
        val tunnelEstablished: Boolean,
        val vpnAuthorized: Boolean,
        val connectivityRiskAccepted: Boolean,
        val lastHeartbeat: Long,
        val lastStarted: Long,
        val lastStopped: Long,
        val lastUpdated: Long,
        val lastError: String?,
        val packetsAudited: Long,
        val packetsAllowed: Long,
        val packetsBlocked: Long,
        val bytesIn: Long,
        val bytesOut: Long,
        val lastPacketTime: Long,
        val persistenceAvailable: Boolean
    ) {
        fun hasFreshHeartbeat(now: Long = System.currentTimeMillis()): Boolean =
            lastHeartbeat > 0L && now >= lastHeartbeat && now - lastHeartbeat <= HEARTBEAT_TIMEOUT_MS

        fun runtimeStatus(now: Long = System.currentTimeMillis()): NetShieldRuntimeStatus {
            if (!persistenceAvailable) return NetShieldRuntimeStatus.UNKNOWN_STALE
            return when (state) {
                STATE_STOPPED -> if (!serviceRunning) {
                    NetShieldRuntimeStatus.STOPPED
                } else {
                    NetShieldRuntimeStatus.UNKNOWN_STALE
                }
                STATE_STARTING -> if (isTransitionFresh(now)) {
                    NetShieldRuntimeStatus.STARTING
                } else {
                    NetShieldRuntimeStatus.UNKNOWN_STALE
                }
                STATE_RUNNING -> if (serviceRunning && tunnelEstablished && hasFreshHeartbeat(now)) {
                    NetShieldRuntimeStatus.RUNNING
                } else {
                    NetShieldRuntimeStatus.UNKNOWN_STALE
                }
                STATE_STOPPING -> if (isTransitionFresh(now)) {
                    NetShieldRuntimeStatus.STOPPING
                } else {
                    NetShieldRuntimeStatus.UNKNOWN_STALE
                }
                STATE_ERROR -> NetShieldRuntimeStatus.ERROR
                else -> NetShieldRuntimeStatus.UNKNOWN_STALE
            }
        }

        private fun isTransitionFresh(now: Long): Boolean =
            lastUpdated > 0L && now >= lastUpdated && now - lastUpdated <= HEARTBEAT_TIMEOUT_MS
    }

    @JvmStatic
    fun read(context: Context): Snapshot {
        val store = store(context)
        return when (val result = store.read()) {
            is NetShieldRuntimeStore.ReadResult.Available -> result.snapshot
            NetShieldRuntimeStore.ReadResult.Missing -> initializeOrUnknown(context, store)
            NetShieldRuntimeStore.ReadResult.Unavailable -> unknownSnapshot()
        }
    }

    @JvmStatic
    fun markVpnAuthorized(context: Context, authorized: Boolean): Boolean =
        preparedStore(context).markVpnAuthorized(authorized)

    @JvmStatic
    fun markConnectivityRiskAccepted(context: Context, accepted: Boolean): Boolean =
        preparedStore(context).markConnectivityRiskAccepted(accepted)

    @JvmStatic
    fun markStarting(context: Context, message: String?): Boolean =
        preparedStore(context).markStarting(message)

    @JvmStatic
    fun markActive(context: Context): Boolean = preparedStore(context).markRunning()

    @JvmStatic
    fun markHeartbeat(context: Context): Boolean = preparedStore(context).markHeartbeat()

    @JvmStatic
    fun markPacketObserved(context: Context, bytes: Int, allowed: Boolean): Boolean =
        preparedStore(context).markPacketObserved(bytes, allowed)

    @JvmStatic
    fun markStopping(context: Context): Boolean = preparedStore(context).markStopping()

    @JvmStatic
    fun markStopped(context: Context, message: String?): Boolean =
        preparedStore(context).markStopped(message)

    @JvmStatic
    fun markError(context: Context, message: String?): Boolean =
        preparedStore(context).markError(message)

    internal fun unknownSnapshot(): Snapshot = Snapshot(
        state = STATE_UNKNOWN,
        serviceRunning = false,
        tunnelEstablished = false,
        vpnAuthorized = false,
        connectivityRiskAccepted = false,
        lastHeartbeat = 0L,
        lastStarted = 0L,
        lastStopped = 0L,
        lastUpdated = 0L,
        lastError = null,
        packetsAudited = 0L,
        packetsAllowed = 0L,
        packetsBlocked = 0L,
        bytesIn = 0L,
        bytesOut = 0L,
        lastPacketTime = 0L,
        persistenceAvailable = false
    )

    private fun initializeOrUnknown(context: Context, store: NetShieldRuntimeStore): Snapshot {
        val prefs = legacyPrefs(context)
        if (prefs.getBoolean(KEY_STORE_INITIALIZED, false)) return unknownSnapshot()

        val now = System.currentTimeMillis()
        val initial = legacySnapshot(context, now) ?: unknownSnapshot().copy(
            state = STATE_STOPPED,
            lastStopped = now,
            lastUpdated = now,
            persistenceAvailable = true
        )
        if (!store.initialize(initial)) return unknownSnapshot()
        prefs.edit().putBoolean(KEY_STORE_INITIALIZED, true).commit()
        return (store.read() as? NetShieldRuntimeStore.ReadResult.Available)?.snapshot ?: unknownSnapshot()
    }

    private fun preparedStore(context: Context): NetShieldRuntimeStore {
        val store = store(context)
        if (store.read() is NetShieldRuntimeStore.ReadResult.Missing) {
            initializeOrUnknown(context, store)
        }
        return store
    }

    private fun legacySnapshot(context: Context, now: Long): Snapshot? {
        val prefs = legacyPrefs(context)
        if (!prefs.contains("state") && !prefs.contains("last_heartbeat")) return null
        val state = when (prefs.getString("state", "INACTIVE")) {
            "ACTIVE" -> STATE_RUNNING
            "STARTING" -> STATE_STARTING
            "STOPPING" -> STATE_STOPPING
            "ERROR" -> STATE_ERROR
            else -> STATE_STOPPED
        }
        val lastHeartbeat = prefs.getLong("last_heartbeat", 0L)
        val lastStarted = prefs.getLong("last_started", 0L)
        val lastStopped = prefs.getLong("last_stopped", 0L)
        return Snapshot(
            state = state,
            serviceRunning = prefs.getBoolean("service_running", false),
            tunnelEstablished = false,
            vpnAuthorized = prefs.getBoolean("vpn_authorized", false),
            connectivityRiskAccepted = prefs.getBoolean("connectivity_risk_accepted", false),
            lastHeartbeat = lastHeartbeat,
            lastStarted = lastStarted,
            lastStopped = lastStopped,
            lastUpdated = maxOf(lastHeartbeat, lastStarted, lastStopped).takeIf { it > 0L } ?: now,
            lastError = prefs.getString("last_error", null),
            packetsAudited = prefs.getLong("packets_audited", 0L),
            packetsAllowed = prefs.getLong("packets_allowed", 0L),
            packetsBlocked = prefs.getLong("packets_blocked", 0L),
            bytesIn = prefs.getLong("bytes_in", 0L),
            bytesOut = prefs.getLong("bytes_out", 0L),
            lastPacketTime = prefs.getLong("last_packet_time", 0L),
            persistenceAvailable = true
        )
    }

    private fun store(context: Context) = NetShieldRuntimeStore(
        context.applicationContext.noBackupFilesDir.resolve(STATE_FILE_NAME)
    )

    private fun legacyPrefs(context: Context) =
        context.applicationContext.getSharedPreferences(LEGACY_PREFS_NAME, Context.MODE_PRIVATE)
}
