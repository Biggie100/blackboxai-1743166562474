package com.advancedquiz.achievements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.advancedquiz.leaderboard.model.AchievementTier
import com.advancedquiz.quiz.ProgressTracker

@Composable
fun AchievementFilters(
    modifier: Modifier = Modifier,
    progressTracker: ProgressTracker = hiltViewModel(),
    onFilterChanged: (AchievementFilter) -> Unit
) {
    val filters = remember {
        listOf(
            AchievementFilter.All,
            AchievementFilter.Tier(AchievementTier.BRONZE),
            AchievementFilter.Tier(AchievementTier.SILVER),
            AchievementFilter.Tier(AchievementTier.GOLD),
            AchievementFilter.Tier(AchievementTier.PLATINUM),
            AchievementFilter.Unlocked,
            AchievementFilter.Locked
        )
    }
    
    var selectedFilter by remember { mutableStateOf<AchievementFilter>(AchievementFilter.All) }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = {
                    selectedFilter = filter
                    onFilterChanged(filter)
                },
                label = { Text(filter.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = when (filter) {
                        is AchievementFilter.Tier -> when (filter.tier) {
                            AchievementTier.BRONZE -> Color(0xFFCD7F32)
                            AchievementTier.SILVER -> Color(0xFFC0C0C0)
                            AchievementTier.GOLD -> Color(0xFFFFD700)
                            AchievementTier.PLATINUM -> Color(0xFFE5E4E2)
                        }
                        else -> MaterialTheme.colorScheme.primary
                    }.copy(alpha = 0.2f),
                    selectedLabelColor = Color.Black
                )
            )
        }
    }
}

sealed class AchievementFilter(val displayName: String) {
    object All : AchievementFilter("All")
    class Tier(val tier: AchievementTier) : AchievementFilter(tier.name)
    object Unlocked : AchievementFilter("Unlocked")
    object Locked : AchievementFilter("Locked")
}