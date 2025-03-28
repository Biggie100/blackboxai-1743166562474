package com.advancedquiz.quiz.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.advancedquiz.quiz.Achievement

@Composable
fun AchievementShareDialog(
    achievement: Achievement,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share Achievement") },
        text = {
            Column {
                Text("Share your ${achievement.title} achievement with friends!")
                Spacer(modifier = Modifier.height(16.dp))
                AchievementBadge(
                    achievement = achievement,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val shareText = "I just unlocked the '${achievement.title}' achievement in Advanced Quiz App! ${achievement.description} #AdvancedQuiz"
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    onDismiss()
                }
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}