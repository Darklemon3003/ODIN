package com.odin.core.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odin.core.netshield.NetShieldController
import com.odin.core.ui.model.NetShieldUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetShieldViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val controller = NetShieldController(context)

    private val _uiState = MutableStateFlow(controller.readState())
    val uiState: StateFlow<NetShieldUiState> = _uiState.asStateFlow()

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
            delay(750)
            refresh()
        }
    }

    fun createVpnAuthorizationIntent(): Intent? {
        val intent = controller.createVpnAuthorizationIntent()
        refresh()
        return intent
    }

    fun onVpnAuthorizationResult(granted: Boolean) {
        controller.onVpnAuthorizationResult(granted)
        refresh()
    }

    fun onNotificationPermissionResult(granted: Boolean) {
        controller.setNotificationConsent(granted)
        refresh()
    }

    fun acceptConnectivityRisk() {
        controller.acceptConnectivityRisk()
        refresh()
    }

    fun startNetShield() {
        viewModelScope.launch {
            controller.startNetShield()
            delay(1_000)
            refresh()
        }
    }

    fun stopNetShield() {
        viewModelScope.launch {
            controller.stopNetShield()
            delay(1_000)
            refresh()
        }
    }
}
