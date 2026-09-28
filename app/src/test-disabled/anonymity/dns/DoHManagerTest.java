package com.odin.core.network.anonymity.dns;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for DoHManager.
 * Enterprise-grade testing for ODIN.
 */
@RunWith(RobolectricTestRunner.class)
public class DoHManagerTest {

    private DoHManager dohManager;

    @Before
    public void setUp() {
        dohManager = new DoHManager();
    }

    @Test
    public void testDoHManagerInitialization() {
        assertNotNull(dohManager);
        assertFalse(dohManager.isDoHEnabled());
        assertEquals("cloudflare", dohManager.getActiveProvider());
    }

    @Test
    public void testEnableDoH() {
        dohManager.enableDoH();
        assertTrue(dohManager.isDoHEnabled());
    }

    @Test
    public void testDisableDoH() {
        dohManager.enableDoH();
        dohManager.disableDoH();
        assertFalse(dohManager.isDoHEnabled());
    }

    @Test
    public void testSetActiveProvider() {
        dohManager.setActiveProvider("google");
        assertEquals("google", dohManager.getActiveProvider());
    }

    @Test
    public void testGetProvider() {
        DoHManager.DoHProvider provider = dohManager.getProvider("cloudflare");
        assertNotNull(provider);
        assertEquals("Cloudflare", provider.getName());
    }

    @Test
    public void testGetAllProviders() {
        assertNotNull(dohManager.getAllProviders());
        assertTrue(dohManager.getAllProviders().size() > 0);
    }
}
