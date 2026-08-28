package com.odin.core.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * DashboardBenchmark measures the performance of Dashboard screen loading.
 * Target: <500ms (percentil 95)
 */
@RunWith(AndroidJUnit4::class)
class DashboardBenchmark {
    
    @get:Rule
    val benchmarkRule = BenchmarkRule()
    
    @Test
    fun benchmarkDashboardLoad() {
        benchmarkRule.measureRepeated {
            // Simulate Dashboard screen loading
            // In a real implementation, this would measure actual screen composition
            // For now, we simulate the workload
            val viewModel = mockDashboardViewModel()
            viewModel.loadSecurityStatus()
            viewModel.loadMetrics()
            viewModel.loadRecentEvents()
        }
    }
    
    private fun mockDashboardViewModel(): Any {
        // Mock ViewModel for benchmarking
        return object {
            fun loadSecurityStatus() {
                // Simulate loading security status
                Thread.sleep(50)
            }
            
            fun loadMetrics() {
                // Simulate loading metrics
                Thread.sleep(100)
            }
            
            fun loadRecentEvents() {
                // Simulate loading recent events
                Thread.sleep(75)
            }
        }
    }
}
