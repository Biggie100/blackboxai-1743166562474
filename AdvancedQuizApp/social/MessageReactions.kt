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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun MessageReactions(
    message: ChatMessage,
    onReactionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
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
                onClick = { onReactionSelected(reaction.emoji) }
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
                    onReactionSelected(emoji)
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

@Composable
private fun ReactionPicker(
    onDismiss: () -> Unit,
    onReactionSelected: (String) -> Unit
) {
    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                val commonReactions = listOf("👍", "❤️", "😂", "😮", "😢", "🙏")
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonReactions.forEach { emoji ->
                        Text(
                            text = emoji,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .clickable {
                                    onReactionSelected(emoji)
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

data class MessageReaction(
    val emoji: String,
    val count: Int,
    val userIds: List<String> = emptyList()
)