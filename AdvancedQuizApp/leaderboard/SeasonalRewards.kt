package com.advancedquiz.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.theme.Purple80
import com.advancedquiz.rewards.NftRewardManager

@Composable
fun SeasonalRewards(
    season: Season,
    modifier: Modifier = Modifier,
    nftRewardManager: NftRewardManager = hiltViewModel()
) {
    val rewards = remember(season.name) { getSeasonalRewards(season) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple80.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Season Rewards",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Purple40
            )
            Spacer(modifier = Modifier.height(8.dp))
            rewards.forEach { reward ->
                SeasonalRewardItem(reward = reward, nftRewardManager = nftRewardManager)
            }
        }
    }
}

@Composable
private fun SeasonalRewardItem(
    reward: SeasonalReward,
    nftRewardManager: NftRewardManager
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Top ${reward.rankRequired}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = reward.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Button(
            onClick = { nftRewardManager.mintRewardNft(reward.nftId) },
            enabled = reward.isClaimable
        ) {
            Text("Claim Reward")
        }
    }
}

private fun getSeasonalRewards(season: Season): List<SeasonalReward> {
    return listOf(
        SeasonalReward(
            seasonName = season.name,
            rankRequired = 1,
            description = "Exclusive Champion NFT",
            nftId = "season_${season.name}_champion",
            isClaimable = false
        ),
        SeasonalReward(
            seasonName = season.name,
            rankRequired = 3,
            description = "Rare Top 3 NFT",
            nftId = "season_${season.name}_top3",
            isClaimable = false
        ),
        SeasonalReward(
            seasonName = season.name,
            rankRequired = 10,
            description = "Season Participant NFT",
            nftId = "season_${season.name}_participant",
            isClaimable = false
        )
    )
}

data class SeasonalReward(
    val seasonName: String,
    val rankRequired: Int,
    val description: String,
    val nftId: String,
    var isClaimable: Boolean
)