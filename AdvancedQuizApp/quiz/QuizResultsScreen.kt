package com.advancedquiz.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import com.advancedquiz.quiz.components.ProgressChart
import com.advancedquiz.quiz.components.AchievementBadge
import com.advancedquiz.quiz.components.AchievementToast
import com.advancedquiz.quiz.components.AchievementShareDialog
import com.advancedquiz.rewards.NftRewardDialog

@Composable
fun QuizResultsScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
    progressTracker: ProgressTracker = hiltViewModel(),
    onContinue: () -> Unit
) {
    var showShareDialog by remember { mutableStateOf(false) }
    var achievementToShare by remember { mutableStateOf<Achievement?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Quiz Complete",
                tint = Purple80,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Quiz Complete!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ResultItem(
                        label = "Score",
                        value = viewModel.score.toString()
                    )
                    ResultItem(
                        label = "Correct Answers",
                        value = "${viewModel.score / 10} out of ${viewModel.getRegularQuestions().size}"
                    )
                    ResultItem(
                        label = "Level Completed",
                        value = "Level ${viewModel.level}"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ProgressChart(
                progressData = progressTracker.weeklyProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            GradientButton(
                colors = listOf(Purple80, Purple40),
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth(),
                text = "Continue"
            )
        }

        if (viewModel.showNftReward) {
            NftRewardDialog(
                nft = viewModel.currentNftReward,
                onDismiss = { viewModel.dismissNftReward() }
            )
        }

        viewModel.unlockedAchievement?.let { achievement ->
            AchievementToast(
                achievement = achievement,
                onDismiss = {
                    viewModel.unlockedAchievement = null
                    achievementToShare = achievement
                    showShareDialog = true
                }
            )
        }

        if (showShareDialog && achievementToShare != null) {
            AchievementShareDialog(
                achievement = achievementToShare!!,
                onDismiss = { showShareDialog = false }
            )
        }
    }
}

@Composable
private fun ResultItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Purple40
        )
    }
}