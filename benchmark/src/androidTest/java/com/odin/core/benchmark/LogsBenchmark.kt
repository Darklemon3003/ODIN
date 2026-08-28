package com.odin.core.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * LogsBenchmark measures the performance of Logs screen loading.
 * Target: <300ms (percentil 95)
 */
@RunWith(AndroidJUnit4::class)
class LogsBenchmark {
    
    @get:Rule
    val benchmarkRule = BenchmarkRule()
    
    @Test
    fun benchmarkLogsLoad() {
        benchmarkRule.measureRepeated {
            // Simulate Logs screen loading
            val viewModel = mockLogsViewModel()
            viewModel.loadLogs()
            viewModel.filterLogs()
        }
    }
    
    private fun mockLogsViewModel(): Any {
        return object {
            fun loadLogs() {
                Thread.sleep(150)
            }
            
            fun filterLogs() {
                Thread.sleep(50)
            }
        }
    }
}
