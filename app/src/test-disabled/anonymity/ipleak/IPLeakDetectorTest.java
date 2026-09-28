package com.odin.core.network.anonymity.ipleak;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for IPLeakDetector.
 * Enterprise-grade testing for ODIN.
 */
@RunWith(RobolectricTestRunner.class)
public class IPLeakDetectorTest {

    @Mock
    private Context mockContext;

    private IPLeakDetector ipLeakDetector;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ipLeakDetector = new IPLeakDetector(mockContext);
    }

    @Test
    public void testIPLeakDetectorInitialization() {
        assertNotNull(ipLeakDetector);
    }

    @Test
    public void testDetectLeaks() {
        IPLeakDetector.LeakDetectionResult result = ipLeakDetector.detectLeaks();
        assertNotNull(result);
        assertNotNull(result.getDetectedIPs());
        assertNotNull(result.getIPv6Addresses());
        assertNotNull(result.getDNSServers());
    }

    @Test
    public void testLeakDetectionResultInitialization() {
        IPLeakDetector.LeakDetectionResult result = new IPLeakDetector.LeakDetectionResult();
        assertNotNull(result.getDetectedIPs());
        assertNotNull(result.getIPv6Addresses());
        assertNotNull(result.getDNSServers());
        assertFalse(result.hasAnyLeaks());
    }

    @Test
    public void testLeakDetectionResultHasAnyLeaks() {
        IPLeakDetector.LeakDetectionResult result = new IPLeakDetector.LeakDetectionResult();
        result.setHasIPLeak(true);
        assertTrue(result.hasAnyLeaks());
    }
}
