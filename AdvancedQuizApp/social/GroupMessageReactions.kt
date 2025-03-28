package com.advancedquiz.social

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun GroupMessageReactions(
    message: ChatMessage,
    groupId: String,
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = hiltViewModel()
) {
    var showReactionPicker by remember { mutableStateOf(false) }
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Display existing reactions
        message.reactions.forEach { reaction ->
            ReactionChip(
                emoji = reaction.emoji,
                count = reaction.count,
                onClick = { 
                    viewModel.toggleGroupMessageReaction(
                        groupId = groupId,
                        messageId = message.id,
                        emoji = reaction.emoji
                    )
                }
            )
        }
        
        // Add reaction button
        IconButton(
            onClick = { showReactionPicker = true },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddReaction,
                contentDescription = "Add reaction",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        // Reaction picker popup
        if (showReactionPicker) {
            ReactionPicker(
                onDismiss = { showReactionPicker = false },
                onReactionSelected = { emoji ->
                    viewModel.addGroupMessageReaction(
                        groupId = groupId,
                        messageId = message.id,
                        emoji = emoji
                    )
                    showReactionPicker = false
                }
            )
        }
    }
}

@Composable
private fun ReactionChip(
    emoji: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji)
            if (count > 1) {
                Spacer(Modifier.width(4.dp))
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}