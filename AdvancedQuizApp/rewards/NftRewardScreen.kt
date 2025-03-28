package com.advancedquiz.rewards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.advancedquiz.quiz.QuizViewModel

@Composable
fun NftRewardScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel()
) {
    var nfts by remember { mutableStateOf<List<NftReward>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        nfts = viewModel.nftRewardManager.getOwnedNfts()
        isLoading = false
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Your NFT Achievements",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (nfts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No NFT rewards yet. Complete quizzes to earn!")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(nfts) { nft ->
                            NftCard(nft = nft)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NftCard(nft: NftReward) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            AsyncImage(
                model = nft.imageUrl,
                contentDescription = "NFT Reward",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = "Token #${nft.tokenId}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}