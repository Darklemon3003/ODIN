package com.odin.core.network.anonymity.routing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for RoutingSelector.
 * Enterprise-grade testing for ODIN.
 */
@RunWith(RobolectricTestRunner.class)
public class RoutingSelectorTest {

    private RoutingSelector routingSelector;

    @Before
    public void setUp() {
        routingSelector = new RoutingSelector();
    }

    @Test
    public void testRoutingSelectorInitialization() {
        assertNotNull(routingSelector);
        assertEquals(RoutingSelector.RoutingMode.AUTO, routingSelector.getCurrentMode());
    }

    @Test
    public void testSetRoutingMode() {
        routingSelector.setRoutingMode(RoutingSelector.RoutingMode.TOR_ONLY);
        assertEquals(RoutingSelector.RoutingMode.TOR_ONLY, routingSelector.getCurrentMode());
    }

    @Test
    public void testSelectRouteTorOnly() {
        routingSelector.setRoutingMode(RoutingSelector.RoutingMode.TOR_ONLY);
        assertEquals("TOR", routingSelector.selectRoute("example.com"));
    }

    @Test
    public void testSelectRouteVpnOnly() {
        routingSelector.setRoutingMode(RoutingSelector.RoutingMode.VPN_ONLY);
        assertEquals("VPN", routingSelector.selectRoute("example.com"));
    }

    @Test
    public void testSelectRouteDirect() {
        routingSelector.setRoutingMode(RoutingSelector.RoutingMode.DIRECT);
        assertEquals("DIRECT", routingSelector.selectRoute("example.com"));
    }

    @Test
    public void testUpdateRouteLatency() {
        routingSelector.updateRouteLatency("TOR", 300.0);
        assertEquals(300.0, routingSelector.getRouteLatency("TOR"), 0.01);
    }

    @Test
    public void testUpdateRouteReliability() {
        routingSelector.updateRouteReliability("TOR", 0.98);
        assertEquals(0.98, routingSelector.getRouteReliability("TOR"), 0.01);
    }
}
