package com.odin.core.network.anonymity.integration;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.odin.core.network.anonymity.dns.DNSPrivacyService;
import com.odin.core.network.anonymity.ipleak.IPLeakProtection;
import com.odin.core.network.anonymity.model.DnsStatus;
import com.odin.core.network.anonymity.model.IPLeakStatus;
import com.odin.core.network.anonymity.model.RoutingStatus;
import com.odin.core.network.anonymity.model.TorStatus;
import com.odin.core.network.anonymity.model.VpnStatus;
import com.odin.core.network.anonymity.routing.NetworkRoutingEngine;
import com.odin.core.network.anonymity.tor.TorService;
import com.odin.core.network.anonymity.vpn.VpnService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Integration tests for Network Anonymity system.
 * Enterprise-grade integration testing for ODIN.
 */
@RunWith(AndroidJUnit4.class)
public class NetworkAnonymityIntegrationTest {

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testTorServiceCreation() {
        TorService torService = new TorService();
        assertNotNull(torService);
        assertNotNull(torService.getTorStatus());
    }

    @Test
    public void testVpnServiceCreation() {
        VpnService vpnService = new VpnService();
        assertNotNull(vpnService);
        assertNotNull(vpnService.getVpnStatus());
    }

    @Test
    public void testNetworkRoutingEngineCreation() {
        NetworkRoutingEngine routingEngine = new NetworkRoutingEngine(context);
        assertNotNull(routingEngine);
        assertNotNull(routingEngine.getRoutingStatus());
    }

    @Test
    public void testDNSPrivacyServiceCreation() {
        DNSPrivacyService dnsPrivacyService = new DNSPrivacyService(context);
        assertNotNull(dnsPrivacyService);
        assertNotNull(dnsPrivacyService.getDnsStatus());
    }

    @Test
    public void testIPLeakProtectionCreation() {
        IPLeakProtection ipLeakProtection = new IPLeakProtection(context);
        assertNotNull(ipLeakProtection);
        assertNotNull(ipLeakProtection.getIPLeakStatus());
    }

    @Test
    public void testTorStatusModel() {
        TorStatus torStatus = new TorStatus();
        assertNotNull(torStatus);
        assertFalse(torStatus.isConnected());
        assertFalse(torStatus.isConnecting());
    }

    @Test
    public void testVpnStatusModel() {
        VpnStatus vpnStatus = new VpnStatus();
        assertNotNull(vpnStatus);
        assertFalse(vpnStatus.isConnected());
        assertFalse(vpnStatus.isConnecting());
    }

    @Test
    public void testRoutingStatusModel() {
        RoutingStatus routingStatus = new RoutingStatus();
        assertNotNull(routingStatus);
        assertFalse(routingStatus.isActive());
    }

    @Test
    public void testDnsStatusModel() {
        DnsStatus dnsStatus = new DnsStatus();
        assertNotNull(dnsStatus);
        assertFalse(dnsStatus.isActive());
    }

    @Test
    public void testIPLeakStatusModel() {
        IPLeakStatus ipLeakStatus = new IPLeakStatus();
        assertNotNull(ipLeakStatus);
        assertFalse(ipLeakStatus.isActive());
        assertFalse(ipLeakStatus.hasAnyLeaks());
    }

    @Test
    public void testNetworkRoutingEngineStartStop() {
        NetworkRoutingEngine routingEngine = new NetworkRoutingEngine(context);
        routingEngine.startEngine();
        assertTrue(routingEngine.isEngineActive());
        
        routingEngine.stopEngine();
        assertFalse(routingEngine.isEngineActive());
    }

    @Test
    public void testDNSPrivacyServiceStartStop() {
        DNSPrivacyService dnsPrivacyService = new DNSPrivacyService(context);
        dnsPrivacyService.startService();
        assertTrue(dnsPrivacyService.isServiceActive());
        
        dnsPrivacyService.stopService();
        assertFalse(dnsPrivacyService.isServiceActive());
    }

    @Test
    public void testIPLeakProtectionStartStop() {
        IPLeakProtection ipLeakProtection = new IPLeakProtection(context);
        ipLeakProtection.startProtection();
        assertTrue(ipLeakProtection.isProtectionActive());
        
        ipLeakProtection.stopProtection();
        assertFalse(ipLeakProtection.isProtectionActive());
    }
}
