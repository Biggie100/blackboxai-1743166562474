package com.advancedquiz.core.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.theme.Purple80
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WalletConnectAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 100.dp,
    strokeWidth: Dp = 4.dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.size(circleSize)) {
        // Background gradient circle
        val gradient = Brush.radialGradient(
            colors = listOf(Purple80, Purple40),
            center = center,
            radius = size.minDimension * 0.5f * pulse
        )

        // Outer rotating circle
        rotate(rotationAngle) {
            drawCircle(
                brush = gradient,
                radius = size.minDimension * 0.4f,
                center = center,
                style = Stroke(strokeWidth.toPx())
            )
        }

        // Inner static circle
        drawCircle(
            color = Purple40,
            radius = size.minDimension * 0.2f,
            center = center
        )

        // WalletConnect logo
        val logoPath = Path().apply {
            moveTo(center.x - 20f, center.y)
            lineTo(center.x + 20f, center.y)
            moveTo(center.x, center.y - 20f)
            lineTo(center.x, center.y + 20f)
            addOval(
                Rect(
                    center = Offset(center.x, center.y),
                    radius = 15f
                )
            )
        }

        drawPath(
            path = logoPath,
            color = Color.White,
            style = Stroke(3f)
        )
    }
}