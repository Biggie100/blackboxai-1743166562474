package com.advancedquiz.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.theme.Purple80

@Composable
fun QuizModeSelector(
    onRegularSelected: () -> Unit,
    onArSelected: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Quiz Mode",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        GradientButton(
            colors = listOf(Purple80, Purple40),
            onClick = {
                viewModel.loadArQuestions()
                onArSelected()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            text = "Augmented Reality Mode",
            textColor = Color.White,
            fontSize = 18.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onRegularSelected,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            border = BorderStroke(2.dp, Purple40)
        ) {
            Text(
                text = "Regular Quiz Mode",
                color = Purple40,
                fontSize = 18.dp
            )
        }
    }
}