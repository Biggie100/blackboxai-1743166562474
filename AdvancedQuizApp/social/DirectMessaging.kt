package com.advancedquiz.social

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.theme.Purple80
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DirectMessagingScreen(
    recipient: SocialUser,
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = hiltViewModel()
) {
    val conversationState by viewModel.getConversation(recipient.address).collectAsState()
    var messageText by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        MessageHeader(recipient = recipient)
        MessagesList(messages = conversationState.messages)
        MessageInput(
            messageText = messageText,
            onMessageChange = { messageText = it },
            onSend = {
                viewModel.sendMessage(recipient.address, messageText)
                messageText = ""
            }
        )
    }
}

@Composable
private fun MessageHeader(recipient: SocialUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Purple80.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = "Messages",
                tint = Purple40,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Chat with ${recipient.userName}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Level ${recipient.level} • ${recipient.points} points",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun MessagesList(messages: List<ChatMessage>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 16.dp),
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages) { message ->
            MessageBubble(message = message)
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (message.isSent) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = if (message.isSent) 16.dp else 0.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = if (message.isSent) 0.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isSent) Purple80 else Purple40.copy(alpha = 0.2f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    color = if (message.isSent) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = SimpleDateFormat("h:mm a", Locale.getDefault())
                        .format(Date(message.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (message.isSent) Color.White.copy(alpha = 0.7f) 
                           else Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun MessageInput(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
            trailingIcon = {
                IconButton(
                    onClick = onSend,
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (messageText.isNotBlank()) Purple80 else Color.Gray
                    )
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}