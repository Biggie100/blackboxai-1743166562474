package com.advancedquiz.social

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TypingIndicator(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf(0, 1, 2).forEach { index ->
            TypingDot(delay = index * 200)
        }
    }
}

@Composable
private fun TypingDot(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.3f at delay
                1f at delay + 300
                0.3f at delay + 600
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        modifier = Modifier.size(8.dp),
        shape = CircleShape,
        color = Color.Gray.copy(alpha = alpha)
    ) {}
}