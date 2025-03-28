package com.advancedquiz.social

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun GroupChatScreen(
    groupId: String,
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = hiltViewModel()
) {
    val groupState by viewModel.getGroupChat(groupId).collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        GroupHeader(group = groupState.group)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MessagesList(
                messages = groupState.messages,
                isGroupChat = true
            )
            
            // Show typing indicators for group members
            if (groupState.typingMembers.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 8.dp)
                ) {
                    groupState.typingMembers.forEach { member ->
                        TypingIndicator(
                            isVisible = true,
                            modifier = Modifier.padding(bottom = 4.dp),
                            label = "${member.userName} is typing..."
                        )
                    }
                }
            }
        }
        MessageInput(
            onSend = { message -> viewModel.sendGroupMessage(groupId, message) },
            onTyping = { isTyping -> 
                viewModel.setGroupTypingStatus(groupId, isTyping) 
            }
        )
    }
}

@Composable
private fun GroupHeader(group: Group) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Purple80.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = "Group",
                    tint = Purple40,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${group.members.size} members",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

data class Group(
    val id: String,
    val name: String,
    val members: List<SocialUser>,
    val createdAt: Long = System.currentTimeMillis()
)

data class GroupChatState(
    val group: Group,
    val messages: List<ChatMessage>
)