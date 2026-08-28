package com.odin.core.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odin.core.forensics.LogExporter
import com.odin.core.network.detection.ThreatDetectionEngine
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * BaseIntegrationTest provides common configuration for all integration tests.
 * This class sets up Hilt dependency injection and provides access to core components.
 */
@HiltAndroidTest
abstract class BaseIntegrationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    // Core components for integration testing
    @Inject
    lateinit var logExporter: LogExporter
    
    @Inject
    lateinit var threatDetectionEngine: ThreatDetectionEngine
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
}
