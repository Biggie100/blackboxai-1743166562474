package com.advancedquiz.auth

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog

@Composable
fun SessionApprovalDialog(
    isOpen: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isOpen) {
        Dialog(onDismissRequest = onReject) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = modifier
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Wallet Connection Request",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "A wallet connection request has been received. Do you want to approve it?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onReject) {
                            Text("Reject")
                        }
                        TextButton(onClick = onApprove) {
                            Text("Approve")
                        }
                    }
                }
            }
        }
    }
}