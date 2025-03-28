package com.advancedquiz.social

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GroupChatWithSettings(
    groupId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = hiltViewModel()
) {
    val groupState by viewModel.getGroupChat(groupId).collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(groupState.group.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    SoundSettingsFeature(viewModel = viewModel)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                MessagesList(
                    messages = groupState.messages,
                    isGroupChat = true
                )
                GroupTypingIndicator(
                    groupId = groupId,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
            MessageInput(
                onSend = { message -> 
                    viewModel.sendGroupMessage(groupId, message)
                    viewModel.groupTypingManager.setTypingStatus(groupId, viewModel.currentUserId, false)
                },
                onTyping = { isTyping ->
                    viewModel.groupTypingManager.setTypingStatus(groupId, viewModel.currentUserId, isTyping)
                }
            )
        }
    }
}