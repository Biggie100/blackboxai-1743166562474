package com.advancedquiz.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.theme.Purple80
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SeasonalLeaderboard(
    modifier: Modifier = Modifier,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val currentSeason = remember { getCurrentSeason() }
    val leaderboardState = viewModel.getSeasonalLeaderboard(currentSeason).collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        SeasonHeader(currentSeason = currentSeason)
        LeaderboardContent(entries = leaderboardState.value)
    }
}

@Composable
private fun SeasonHeader(currentSeason: Season) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Purple80.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = currentSeason.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Purple40
                )
                Text(
                    text = "${currentSeason.startDate} - ${currentSeason.endDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = "${daysRemaining(currentSeason.endDate)} days remaining",
                style = MaterialTheme.typography.labelLarge,
                color = Purple40
            )
        }
    }
}

private fun getCurrentSeason(): Season {
    val now = LocalDate.now()
    val year = now.year
    return when {
        now.monthValue in 1..3 -> Season("Winter $year", "$year-01-01", "$year-03-31")
        now.monthValue in 4..6 -> Season("Spring $year", "$year-04-01", "$year-06-30")
        now.monthValue in 7..9 -> Season("Summer $year", "$year-07-01", "$year-09-30")
        else -> Season("Fall $year", "$year-10-01", "$year-12-31")
    }
}

private fun daysRemaining(endDate: String): Long {
    val formatter = DateTimeFormatter.ISO_DATE
    return LocalDate.parse(endDate, formatter).toEpochDay() - LocalDate.now().toEpochDay()
}

data class Season(
    val name: String,
    val startDate: String,
    val endDate: String
)