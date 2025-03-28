package com.advancedquiz.quiz.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.advancedquiz.quiz.DailyProgress

@Composable
fun ProgressChart(
    progressData: List<DailyProgress>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weekly Progress",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            progressData.forEach { progress ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    val barHeight = progress.progressPercent * 180.dp
                    val color = if (progress.progressPercent >= 0.8f) {
                        Color(0xFF4CAF50) // Green for high performance
                    } else if (progress.progressPercent >= 0.5f) {
                        Color(0xFFFFC107) // Yellow for medium performance
                    } else {
                        Color(0xFFF44336) // Red for low performance
                    }

                    Canvas(
                        modifier = Modifier
                            .width(24.dp)
                            .fillMaxHeight()
                    ) {
                        drawRect(
                            color = color.copy(alpha = 0.2f),
                            topLeft = Offset(0f, size.height - barHeight.toPx()),
                            size = Size(size.width, barHeight.toPx())
                        )
                        drawRect(
                            color = color,
                            topLeft = Offset(0f, size.height - barHeight.toPx()),
                            size = Size(size.width, barHeight.toPx()),
                            style = Stroke(2.dp.toPx())
                        )
                    }

                    Text(
                        text = progress.day,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}