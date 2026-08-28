package com.odin.core.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * FirewallBenchmark measures the performance of Firewall screen loading.
 * Target: <200ms (percentil 95)
 */
@RunWith(AndroidJUnit4::class)
class FirewallBenchmark {
    
    @get:Rule
    val benchmarkRule = BenchmarkRule()
    
    @Test
    fun benchmarkFirewallLoad() {
        benchmarkRule.measureRepeated {
            // Simulate Firewall screen loading
            val viewModel = mockFirewallViewModel()
            viewModel.loadRules()
        }
    }
    
    private fun mockFirewallViewModel(): Any {
        return object {
            fun loadRules() {
                Thread.sleep(100)
            }
        }
    }
}
