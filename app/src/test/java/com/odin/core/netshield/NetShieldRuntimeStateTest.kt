package com.odin.core.netshield

import com.odin.core.ui.model.NetShieldRuntimeStatus
import com.odin.core.ui.model.NetShieldStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class NetShieldRuntimeStateTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `fresh running heartbeat maps to running`() {
        val snapshot = snapshot(
            state = NetShieldRuntimeState.STATE_RUNNING,
            serviceRunning = true,
            tunnelEstablished = true,
            lastHeartbeat = NOW
        )

        assertEquals(NetShieldRuntimeStatus.RUNNING, snapshot.runtimeStatus(NOW + 1_000L))
    }

    @Test
    fun `explicit stopped state maps to stopped`() {
        val snapshot = snapshot(
            state = NetShieldRuntimeState.STATE_STOPPED,
            lastStopped = NOW,
            lastUpdated = NOW
        )

        assertEquals(NetShieldRuntimeStatus.STOPPED, snapshot.runtimeStatus(NOW + 1_000L))
    }

    @Test
    fun `stale running heartbeat maps to unknown stale`() {
        val snapshot = snapshot(
            state = NetShieldRuntimeState.STATE_RUNNING,
            serviceRunning = true,
            tunnelEstablished = true,
            lastHeartbeat = NOW
        )

        assertEquals(NetShieldRuntimeStatus.UNKNOWN_STALE, snapshot.runtimeStatus(NOW + 90_001L))
    }

    @Test
    fun `running state without heartbeat maps to unknown stale`() {
        val snapshot = snapshot(
            state = NetShieldRuntimeState.STATE_RUNNING,
            serviceRunning = true,
            tunnelEstablished = true,
            lastHeartbeat = 0L
        )

        assertEquals(NetShieldRuntimeStatus.UNKNOWN_STALE, snapshot.runtimeStatus(NOW))
    }

    @Test
    fun `running heartbeat without tunnel evidence maps to unknown stale`() {
        val snapshot = snapshot(
            state = NetShieldRuntimeState.STATE_RUNNING,
            serviceRunning = true,
            tunnelEstablished = false,
            lastHeartbeat = NOW
        )

        assertEquals(NetShieldRuntimeStatus.UNKNOWN_STALE, snapshot.runtimeStatus(NOW))
    }

    @Test
    fun `service start and heartbeat are persisted for a fresh reader`() {
        var clock = NOW
        val stateFile = temporaryFolder.newFolder("start").resolve("runtime.properties")
        val writer = NetShieldRuntimeStore(stateFile) { clock }
        assertTrue(writer.initialize(snapshot(state = NetShieldRuntimeState.STATE_STOPPED)))
        assertTrue(writer.markStarting(null))

        clock += 1_000L
        assertTrue(writer.markRunning())
        val persisted = (NetShieldRuntimeStore(stateFile) { clock }.read() as NetShieldRuntimeStore.ReadResult.Available).snapshot

        assertEquals(NetShieldRuntimeStatus.RUNNING, persisted.runtimeStatus(clock))
        assertTrue(persisted.serviceRunning)
        assertEquals(clock, persisted.lastHeartbeat)
    }

    @Test
    fun `service stop persists explicit stop evidence`() {
        var clock = NOW
        val stateFile = temporaryFolder.newFolder("stop").resolve("runtime.properties")
        val store = NetShieldRuntimeStore(stateFile) { clock }
        assertTrue(store.initialize(snapshot(state = NetShieldRuntimeState.STATE_STOPPED)))
        assertTrue(store.markRunning())

        clock += 2_000L
        assertTrue(store.markStopping())
        assertTrue(store.markStopped("stopped"))
        val persisted = (store.read() as NetShieldRuntimeStore.ReadResult.Available).snapshot

        assertEquals(NetShieldRuntimeStatus.STOPPED, persisted.runtimeStatus(clock))
        assertFalse(persisted.serviceRunning)
        assertEquals(clock, persisted.lastStopped)
    }

    @Test
    fun `process restart cannot keep stale running state active`() {
        var clock = NOW
        val stateFile = temporaryFolder.newFolder("restart").resolve("runtime.properties")
        val firstProcess = NetShieldRuntimeStore(stateFile) { clock }
        assertTrue(firstProcess.initialize(snapshot(state = NetShieldRuntimeState.STATE_STOPPED)))
        assertTrue(firstProcess.markRunning())

        clock += 90_001L
        val restartedProcess = NetShieldRuntimeStore(stateFile) { clock }
        val persisted = (restartedProcess.read() as NetShieldRuntimeStore.ReadResult.Available).snapshot

        assertEquals(NetShieldRuntimeStatus.UNKNOWN_STALE, persisted.runtimeStatus(clock))
    }

    @Test
    fun `consent does not turn stopped runtime into active UI`() {
        val status = NetShieldStatusResolver.resolve(
            runtimeStatus = NetShieldRuntimeStatus.STOPPED,
            consentGranted = true,
            notificationGranted = true,
            vpnAuthorized = true,
            connectivityRiskAccepted = true
        )

        assertEquals(NetShieldStatus.READY_TO_START, status)
    }

    @Test
    fun `running evidence remains active while consent stop is pending`() {
        val status = NetShieldStatusResolver.resolve(
            runtimeStatus = NetShieldRuntimeStatus.RUNNING,
            consentGranted = false,
            notificationGranted = true,
            vpnAuthorized = true,
            connectivityRiskAccepted = true
        )

        assertEquals(NetShieldStatus.ACTIVE, status)
    }

    private fun snapshot(
        state: String = NetShieldRuntimeState.STATE_UNKNOWN,
        serviceRunning: Boolean = false,
        tunnelEstablished: Boolean = false,
        lastHeartbeat: Long = 0L,
        lastStopped: Long = 0L,
        lastUpdated: Long = NOW
    ) = NetShieldRuntimeState.Snapshot(
        state = state,
        serviceRunning = serviceRunning,
        tunnelEstablished = tunnelEstablished,
        vpnAuthorized = false,
        connectivityRiskAccepted = false,
        lastHeartbeat = lastHeartbeat,
        lastStarted = 0L,
        lastStopped = lastStopped,
        lastUpdated = lastUpdated,
        lastError = null,
        packetsAudited = 0L,
        packetsAllowed = 0L,
        packetsBlocked = 0L,
        bytesIn = 0L,
        bytesOut = 0L,
        lastPacketTime = 0L,
        persistenceAvailable = true
    )

    private companion object {
        const val NOW = 1_000_000L
    }
}
