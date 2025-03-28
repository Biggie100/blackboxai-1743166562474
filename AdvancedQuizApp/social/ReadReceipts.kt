package com.advancedquiz.social

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReadReceipt(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = SimpleDateFormat("h:mm a", Locale.getDefault())
                .format(Date(message.timestamp)),
            style = MaterialTheme.typography.labelSmall,
            color = if (message.isSent) Color.White.copy(alpha = 0.7f) 
                   else Color.Gray
        )
        
        if (message.isSent) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = "Read receipt",
                tint = if (message.isRead) MaterialTheme.colorScheme.primary 
                      else Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}