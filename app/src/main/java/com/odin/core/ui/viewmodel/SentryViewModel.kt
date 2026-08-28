package com.odin.core.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.sentry.SentryController
import com.odin.core.ui.model.SentryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentryViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val controller = SentryController(context)

    private val _uiState = MutableStateFlow(controller.readState())
    val uiState: StateFlow<SentryUiState> = _uiState.asStateFlow()

    fun refresh() {
        _uiState.value = controller.readState()
    }

    fun acceptConsent() {
        controller.grantConsent()
        refresh()
    }

    fun revokeConsent() {
        viewModelScope.launch {
            controller.revokeConsent()
            delay(500)
            refresh()
        }
    }

    fun setBootConsent(enabled: Boolean) {
        controller.setBootConsent(enabled)
        refresh()
    }

    fun onNotificationPermissionResult(granted: Boolean) {
        controller.setNotificationConsent(granted)
        refresh()
    }

    fun startSentry() {
        viewModelScope.launch {
            controller.startSentry()
            delay(1_000)
            refresh()
        }
    }

    fun stopSentry() {
        viewModelScope.launch {
            controller.stopSentry()
            delay(1_000)
            refresh()
        }
    }
}
