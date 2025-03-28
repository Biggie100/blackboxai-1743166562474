package com.advancedquiz.social

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun FlyingEmoji(
    emoji: String,
    modifier: Modifier = Modifier,
    onAnimationComplete: () -> Unit = {}
) {
    val density = LocalDensity.current
    val startX = remember { Random.nextFloat() * 100f }
    val endX = remember { startX + (Random.nextFloat() * 200f - 100f) }
    val startY = remember { 0f }
    val endY = remember { -200f }
    
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val (alpha, scale, x, y) = remember { 
        Animatable(0f) to Animatable(0.5f) to 
        Animatable(startX) to Animatable(startY) 
    }

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(1f, animationSpec = tween(300))
            scale.animateTo(1.2f, animationSpec = spring())
            x.animateTo(endX, animationSpec = tween(1000))
            y.animateTo(endY, animationSpec = tween(1000))
            alpha.animateTo(0f, animationSpec = tween(300))
            onAnimationComplete()
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = with(density) { x.value.toDp().toPx() }
                translationY = with(density) { y.value.toDp().toPx() }
                rotationZ = rotation
            }
            .alpha(alpha.value)
            .scale(scale.value)
    ) {
        Text(
            text = emoji,
            modifier = Modifier.padding(4.dp)
        )
    }
}