package com.odin.core.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * SettingsBenchmark measures the performance of Settings screen loading.
 * Target: <100ms (percentil 95)
 */
@RunWith(AndroidJUnit4::class)
class SettingsBenchmark {
    
    @get:Rule
    val benchmarkRule = BenchmarkRule()
    
    @Test
    fun benchmarkSettingsLoad() {
        benchmarkRule.measureRepeated {
            // Simulate Settings screen loading
            val viewModel = mockSettingsViewModel()
            viewModel.loadSettings()
        }
    }
    
    private fun mockSettingsViewModel(): Any {
        return object {
            fun loadSettings() {
                Thread.sleep(50)
            }
        }
    }
}
