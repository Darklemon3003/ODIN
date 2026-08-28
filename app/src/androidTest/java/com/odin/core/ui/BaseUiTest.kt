package com.odin.core.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

/**
 * BaseUiTest provides common configuration for all UI Compose tests.
 * This class sets up Hilt dependency injection and Compose test rule.
 */
@HiltAndroidTest
abstract class BaseUiTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
}
