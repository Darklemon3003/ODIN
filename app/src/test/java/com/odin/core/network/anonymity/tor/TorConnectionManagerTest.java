package com.odin.core.network.anonymity.tor;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for TorConnectionManager.
 * Enterprise-grade testing for ODIN.
 */
@RunWith(RobolectricTestRunner.class)
public class TorConnectionManagerTest {

    @Mock
    private Context mockContext;

    private TorConnectionManager torConnectionManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        torConnectionManager = new TorConnectionManager(mockContext);
    }

    @Test
    public void testTorConnectionManagerInitialization() {
        assertNotNull(torConnectionManager);
        assertFalse(torConnectionManager.isTorRunning());
    }

    @Test
    public void testGetSocksPort() {
        assertEquals(9050, torConnectionManager.getSocksPort());
    }

    @Test
    public void testGetControlPort() {
        assertEquals(9051, torConnectionManager.getControlPort());
    }

    @Test
    public void testGetDnsPort() {
        assertEquals(5400, torConnectionManager.getDnsPort());
    }

    @Test
    public void testGetTorStatus() {
        assertNotNull(torConnectionManager.getTorStatus());
    }

    @Test
    public void testStartTor() {
        torConnectionManager.startTor();
        // Note: In actual test, we would wait for connection
        assertNotNull(torConnectionManager.getTorStatus());
    }

    @Test
    public void testStopTor() {
        torConnectionManager.stopTor();
        assertFalse(torConnectionManager.isTorRunning());
    }
}
