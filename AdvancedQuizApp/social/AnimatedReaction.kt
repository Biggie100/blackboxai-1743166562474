package com.advancedquiz.social

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedReaction(
    emoji: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var isAnimating by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isAnimating = false }
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clickable {
                isAnimating = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            modifier = Modifier.padding(4.dp)
        )
    }
}