package com.advancedquiz.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancedquiz.core.ui.states.UiState
import kotlinx.coroutines.launch

class Web3AuthViewModel(
    private val walletConnectManager: WalletConnectManager
) : ViewModel() {

    var connectionState by mutableStateOf<UiState<Boolean>>(UiState.Idle)
        private set

    fun connectWallet() {
        viewModelScope.launch {
            connectionState = UiState.Loading
            try {
                walletConnectManager.connect()
                connectionState = UiState.Success(true)
            } catch (e: Exception) {
                connectionState = UiState.Error(e.message ?: "Connection failed")
            }
        }
    }

    fun disconnectWallet() {
        viewModelScope.launch {
            walletConnectManager.disconnect()
            connectionState = UiState.Idle
        }
    }

    fun resetState() {
        connectionState = UiState.Idle
    }
}