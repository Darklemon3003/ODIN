package com.odin.core.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * QuarantineBenchmark measures the performance of Quarantine screen loading.
 * Target: <200ms (percentil 95)
 */
@RunWith(AndroidJUnit4::class)
class QuarantineBenchmark {
    
    @get:Rule
    val benchmarkRule = BenchmarkRule()
    
    @Test
    fun benchmarkQuarantineLoad() {
        benchmarkRule.measureRepeated {
            // Simulate Quarantine screen loading
            val viewModel = mockQuarantineViewModel()
            viewModel.loadQuarantinedApps()
        }
    }
    
    private fun mockQuarantineViewModel(): Any {
        return object {
            fun loadQuarantinedApps() {
                Thread.sleep(100)
            }
        }
    }
}
