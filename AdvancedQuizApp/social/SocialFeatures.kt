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
import com.advancedquiz.auth.WalletConnectManager
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.theme.Purple80

@Composable
fun SocialFeaturesScreen(
    modifier: Modifier = Modifier,
    viewModel: SocialViewModel = hiltViewModel()
) {
    val socialState by viewModel.socialState.collectAsState()
    val walletManager: WalletConnectManager = hiltViewModel()

    Column(modifier = modifier.fillMaxSize()) {
        SocialHeader(walletManager = walletManager)
        SocialContent(socialState = socialState, viewModel = viewModel)
    }
}

@Composable
private fun SocialHeader(walletManager: WalletConnectManager) {
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
                imageVector = Icons.Default.People,
                contentDescription = "Social",
                tint = Purple40,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Quiz Community",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = walletManager.session?.walletAddress?.let { 
                        "Connected as ${it.take(6)}...${it.takeLast(4)}" 
                    } ?: "Guest User",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun SocialContent(
    socialState: SocialState,
    viewModel: SocialViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Friends Activity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(socialState.friendsActivity) { activity ->
            SocialActivityItem(activity = activity)
        }
        
        item {
            Text(
                text = "Suggested Friends",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(socialState.suggestedFriends) { friend ->
            SuggestedFriendItem(
                friend = friend,
                onFollow = { viewModel.followUser(friend.address) }
            )
        }
    }
}

@Composable
private fun SocialActivityItem(activity: SocialActivity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (activity.type) {
                    SocialActivityType.ACHIEVEMENT -> Icons.Default.Star
                    SocialActivityType.LEADERBOARD -> Icons.Default.Leaderboard
                    SocialActivityType.QUIZ -> Icons.Default.Quiz
                },
                contentDescription = activity.type.name,
                tint = Purple40
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.userName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = activity.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun SuggestedFriendItem(
    friend: SocialUser,
    onFollow: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = friend.userName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Level ${friend.level} • ${friend.points} points",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Button(
                onClick = onFollow,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple80,
                    contentColor = Color.White
                )
            ) {
                Text("Follow")
            }
        }
    }
}