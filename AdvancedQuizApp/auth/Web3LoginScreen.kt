package com.advancedquiz.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.advancedquiz.core.ui.theme.Purple80
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.components.GradientButton
import com.advancedquiz.core.ui.components.WalletConnectAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Web3LoginScreen(
    state: UiState<Boolean>,
    onWalletConnectClick: () -> Unit,
    onRetryClick: () -> Unit,
    onEmailLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = state is UiState.Loading
    val error = (state as? UiState.Error)?.message
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated WalletConnect logo
            WalletConnectAnimation(
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Connect Your Wallet",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Experience the future of quiz apps with Web3 authentication",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            when (state) {
                is UiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Connection Failed",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error ?: "Unknown error occurred",
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GradientButton(
                            colors = listOf(Purple80, Purple40),
                            onClick = onRetryClick,
                            modifier = Modifier.fillMaxWidth(),
                            text = "Retry Connection"
                        )
                    }
                }
                else -> {
                    GradientButton(
                        colors = listOf(Purple80, Purple40),
                        onClick = onWalletConnectClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Connect with WalletConnect",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Alternative login option
            TextButton(
                onClick = onEmailLoginClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Continue with Email",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}