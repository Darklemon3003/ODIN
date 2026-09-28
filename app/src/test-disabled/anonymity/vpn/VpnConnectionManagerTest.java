package com.odin.core.network.anonymity.vpn;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for VpnConnectionManager.
 * Enterprise-grade testing for ODIN.
 */
@RunWith(RobolectricTestRunner.class)
public class VpnConnectionManagerTest {

    @Mock
    private Context mockContext;

    private VpnConnectionManager vpnConnectionManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        vpnConnectionManager = new VpnConnectionManager(mockContext);
    }

    @Test
    public void testVpnConnectionManagerInitialization() {
        assertNotNull(vpnConnectionManager);
        assertFalse(vpnConnectionManager.isVpnRunning());
    }

    @Test
    public void testGetVpnStatus() {
        assertNotNull(vpnConnectionManager.getVpnStatus());
    }

    @Test
    public void testSetServerAddress() {
        vpnConnectionManager.setServerAddress("vpn.example.com:443");
        assertEquals("vpn.example.com:443", vpnConnectionManager.getVpnStatus().getServerAddress());
    }

    @Test
    public void testUpdateDataStats() {
        vpnConnectionManager.updateDataStats(1024, 512);
        assertEquals(1536, vpnConnectionManager.getVpnStatus().getDataTransferred());
    }

    @Test
    public void testStopVpn() {
        vpnConnectionManager.stopVpn();
        assertFalse(vpnConnectionManager.isVpnRunning());
    }
}
