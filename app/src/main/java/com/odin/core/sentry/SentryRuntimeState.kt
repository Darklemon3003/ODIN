package com.odin.core.sentry

import android.content.Context

object SentryRuntimeState {
    private const val PREFS_NAME = "odin_sentry_runtime"
    private const val KEY_STATE = "state"
    private const val KEY_SERVICE_RUNNING = "service_running"
    private const val KEY_LAST_HEARTBEAT = "last_heartbeat"
    private const val KEY_LAST_STARTED = "last_started"
    private const val KEY_LAST_STOPPED = "last_stopped"
    private const val KEY_LAST_ERROR = "last_error"
    private const val KEY_CORE_HEALTHY = "core_healthy"
    private const val HEARTBEAT_TIMEOUT_MS = 90_000L

    const val STATE_INACTIVE = "INACTIVE"
    const val STATE_STARTING = "STARTING"
    const val STATE_ACTIVE = "ACTIVE"
    const val STATE_STOPPING = "STOPPING"
    const val STATE_ERROR = "ERROR"

    data class Snapshot(
        val state: String,
        val serviceRunning: Boolean,
        val lastHeartbeat: Long,
        val lastStarted: Long,
        val lastStopped: Long,
        val lastError: String?,
        val coreHealthy: Boolean
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
            lastHeartbeat = prefs.getLong(KEY_LAST_HEARTBEAT, 0L),
            lastStarted = prefs.getLong(KEY_LAST_STARTED, 0L),
            lastStopped = prefs.getLong(KEY_LAST_STOPPED, 0L),
            lastError = prefs.getString(KEY_LAST_ERROR, null),
            coreHealthy = prefs.getBoolean(KEY_CORE_HEALTHY, false)
        )
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
    fun markActive(context: Context, coreHealthy: Boolean) {
        val now = System.currentTimeMillis()
        prefs(context).edit()
            .putString(KEY_STATE, STATE_ACTIVE)
            .putBoolean(KEY_SERVICE_RUNNING, true)
            .putLong(KEY_LAST_HEARTBEAT, now)
            .putLong(KEY_LAST_STARTED, now)
            .putString(KEY_LAST_ERROR, null)
            .putBoolean(KEY_CORE_HEALTHY, coreHealthy)
            .apply()
    }

    @JvmStatic
    fun markHeartbeat(context: Context, coreHealthy: Boolean) {
        prefs(context).edit()
            .putLong(KEY_LAST_HEARTBEAT, System.currentTimeMillis())
            .putBoolean(KEY_CORE_HEALTHY, coreHealthy)
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
            .putBoolean(KEY_CORE_HEALTHY, false)
            .apply()
    }

    @JvmStatic
    fun markError(context: Context, message: String?) {
        prefs(context).edit()
            .putString(KEY_STATE, STATE_ERROR)
            .putBoolean(KEY_SERVICE_RUNNING, false)
            .putString(KEY_LAST_ERROR, message)
            .putBoolean(KEY_CORE_HEALTHY, false)
            .apply()
    }

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
