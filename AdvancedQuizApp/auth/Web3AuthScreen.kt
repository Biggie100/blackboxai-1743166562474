package com.advancedquiz.auth

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.advancedquiz.core.ui.states.UiState

@Composable
fun Web3AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: Web3AuthViewModel = hiltViewModel(),
    onAuthSuccess: () -> Unit,
    onEmailAuthClick: () -> Unit
) {
    val state by viewModel.state.observeAsState(initial = UiState.Idle)
    var showSessionDialog by remember { mutableStateOf(false) }
    var currentSessionRequest by remember { mutableStateOf<SessionRequest?>(null) }

    // Initialize WalletConnect
    LaunchedEffect(Unit) {
        viewModel.walletConnectManager.initWalletConnect { request ->
            currentSessionRequest = request
            showSessionDialog = true
        }
    }

    // Session approval dialog
    SessionApprovalDialog(
        isOpen = showSessionDialog,
        onApprove = {
            currentSessionRequest?.let { request ->
                viewModel.walletConnectManager.approveSession(request)
            }
            showSessionDialog = false
        },
        onReject = {
            currentSessionRequest?.let { request ->
                viewModel.walletConnectManager.rejectSession(request)
            }
            showSessionDialog = false
        }
    )

    Web3LoginScreen(
        state = state,
        onWalletConnectClick = { viewModel.connectWallet() },
        onRetryClick = {
            viewModel.resetState()
            viewModel.connectWallet()
        },
        onEmailLoginClick = onEmailAuthClick,
        modifier = modifier
    )

    // Handle successful connection
    if (state is UiState.Success) {
        onAuthSuccess()
    }
}