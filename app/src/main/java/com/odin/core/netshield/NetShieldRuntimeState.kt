package com.odin.core.netshield

import android.content.Context

object NetShieldRuntimeState {
    private const val PREFS_NAME = "odin_netshield_runtime"
    private const val KEY_STATE = "state"
    private const val KEY_SERVICE_RUNNING = "service_running"
    private const val KEY_VPN_AUTHORIZED = "vpn_authorized"
    private const val KEY_CONNECTIVITY_RISK_ACCEPTED = "connectivity_risk_accepted"
    private const val KEY_LAST_HEARTBEAT = "last_heartbeat"
    private const val KEY_LAST_STARTED = "last_started"
    private const val KEY_LAST_STOPPED = "last_stopped"
    private const val KEY_LAST_ERROR = "last_error"
    private const val KEY_PACKETS_AUDITED = "packets_audited"
    private const val KEY_PACKETS_ALLOWED = "packets_allowed"
    private const val KEY_PACKETS_BLOCKED = "packets_blocked"
    private const val KEY_BYTES_IN = "bytes_in"
    private const val KEY_BYTES_OUT = "bytes_out"
    private const val KEY_LAST_PACKET_TIME = "last_packet_time"
    private const val HEARTBEAT_TIMEOUT_MS = 90_000L

    const val STATE_INACTIVE = "INACTIVE"
    const val STATE_STARTING = "STARTING"
    const val STATE_ACTIVE = "ACTIVE"
    const val STATE_STOPPING = "STOPPING"
    const val STATE_ERROR = "ERROR"

    data class Snapshot(
        val state: String,
        val serviceRunning: Boolean,
        val vpnAuthorized: Boolean,
        val connectivityRiskAccepted: Boolean,
        val lastHeartbeat: Long,
        val lastStarted: Long,
        val lastStopped: Long,
        val lastError: String?,
        val packetsAudited: Long,
        val packetsAllowed: Long,
        val packetsBlocked: Long,
        val bytesIn: Long,
        val bytesOut: Long,
        val lastPacketTime: Long
    ) {
        fun hasFreshHeartbeat(now: Long = System.currentTimeMillis()): Boolean {
            return lastHeartbeat > 0L && now - lastHeartbeat <= HEARTBEAT_TIMEOUT_MS
        }
    }

    @JvmStatic
    fun read(context: Context): Snapshot {
        val prefs = prefs(context)
        return Snapshot(
            state = prefs.getString(KEY_STATE, STATE_INACTIVE) ?: STATE_INACTIVE,
            serviceRunning = prefs.getBoolean(KEY_SERVICE_RUNNING, false),
            vpnAuthorized = prefs.getBoolean(KEY_VPN_AUTHORIZED, false),
            connectivityRiskAccepted = prefs.getBoolean(KEY_CONNECTIVITY_RISK_ACCEPTED, false),
            lastHeartbeat = prefs.getLong(KEY_LAST_HEARTBEAT, 0L),
            lastStarted = prefs.getLong(KEY_LAST_STARTED, 0L),
            lastStopped = prefs.getLong(KEY_LAST_STOPPED, 0L),
            lastError = prefs.getString(KEY_LAST_ERROR, null),
            packetsAudited = prefs.getLong(KEY_PACKETS_AUDITED, 0L),
            packetsAllowed = prefs.getLong(KEY_PACKETS_ALLOWED, 0L),
            packetsBlocked = prefs.getLong(KEY_PACKETS_BLOCKED, 0L),
            bytesIn = prefs.getLong(KEY_BYTES_IN, 0L),
            bytesOut = prefs.getLong(KEY_BYTES_OUT, 0L),
            lastPacketTime = prefs.getLong(KEY_LAST_PACKET_TIME, 0L)
        )
    }

    @JvmStatic
    fun markVpnAuthorized(context: Context, authorized: Boolean) {
        prefs(context).edit()
            .putBoolean(KEY_VPN_AUTHORIZED, authorized)
            .putString(KEY_LAST_ERROR, null)
            .apply()
    }

    @JvmStatic
    fun markConnectivityRiskAccepted(context: Context, accepted: Boolean) {
        prefs(context).edit()
            .putBoolean(KEY_CONNECTIVITY_RISK_ACCEPTED, accepted)
            .apply()
    }

    @JvmStatic
    fun markStarting(context: Context, message: String?) {
        prefs(context).edit()
            .putString(KEY_STATE, STATE_STARTING)
            .putBoolean(KEY_SERVICE_RUNNING, false)
            .putString(KEY_LAST_ERROR, message)
            .apply()
    }

    @JvmStatic
    fun markActive(context: Context) {
        val now = System.currentTimeMillis()
        prefs(context).edit()
            .putString(KEY_STATE, STATE_ACTIVE)
            .putBoolean(KEY_SERVICE_RUNNING, true)
            .putLong(KEY_LAST_HEARTBEAT, now)
            .putLong(KEY_LAST_STARTED, now)
            .putString(KEY_LAST_ERROR, null)
            .apply()
    }

    @JvmStatic
    fun markHeartbeat(context: Context) {
        prefs(context).edit()
            .putLong(KEY_LAST_HEARTBEAT, System.currentTimeMillis())
            .apply()
    }

    @JvmStatic
    fun markPacketObserved(context: Context, bytes: Int, allowed: Boolean) {
        val prefs = prefs(context)
        val now = System.currentTimeMillis()
        prefs.edit()
            .putLong(KEY_LAST_HEARTBEAT, now)
            .putLong(KEY_LAST_PACKET_TIME, now)
            .putLong(KEY_PACKETS_AUDITED, prefs.getLong(KEY_PACKETS_AUDITED, 0L) + 1L)
            .putLong(KEY_BYTES_IN, prefs.getLong(KEY_BYTES_IN, 0L) + bytes.toLong())
            .putLong(
                if (allowed) KEY_PACKETS_ALLOWED else KEY_PACKETS_BLOCKED,
                prefs.getLong(if (allowed) KEY_PACKETS_ALLOWED else KEY_PACKETS_BLOCKED, 0L) + 1L
            )
            .apply()
    }

    @JvmStatic
    fun markStopping(context: Context) {
        prefs(context).edit()
            .putString(KEY_STATE, STATE_STOPPING)
            .putBoolean(KEY_SERVICE_RUNNING, false)
            .apply()
    }

    @JvmStatic
    fun markStopped(context: Context, message: String?) {
        prefs(context).edit()
            .putString(KEY_STATE, STATE_INACTIVE)
            .putBoolean(KEY_SERVICE_RUNNING, false)
            .putLong(KEY_LAST_STOPPED, System.currentTimeMillis())
            .putString(KEY_LAST_ERROR, message)
            .apply()
    }

    @JvmStatic
    fun markError(context: Context, message: String?) {
        prefs(context).edit()
            .putString(KEY_STATE, STATE_ERROR)
            .putBoolean(KEY_SERVICE_RUNNING, false)
            .putString(KEY_LAST_ERROR, message)
            .apply()
    }

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
