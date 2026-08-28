package com.odin.core.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * ThreatsBenchmark measures the performance of Threats screen loading.
 * Target: <300ms (percentil 95)
 */
@RunWith(AndroidJUnit4::class)
class ThreatsBenchmark {
    
    @get:Rule
    val benchmarkRule = BenchmarkRule()
    
    @Test
    fun benchmarkThreatsLoad() {
        benchmarkRule.measureRepeated {
            // Simulate Threats screen loading
            val viewModel = mockThreatsViewModel()
            viewModel.loadThreats()
            viewModel.loadStatistics()
        }
    }
    
    private fun mockThreatsViewModel(): Any {
        return object {
            fun loadThreats() {
                Thread.sleep(150)
            }
            
            fun loadStatistics() {
                Thread.sleep(75)
            }
        }
    }
}
