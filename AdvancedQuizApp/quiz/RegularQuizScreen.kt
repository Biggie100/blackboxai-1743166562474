package com.advancedquiz.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.advancedquiz.core.ui.theme.Purple40
import com.advancedquiz.core.ui.theme.Purple80

@Composable
fun RegularQuizScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val questions = remember { viewModel.getRegularQuestions() }
    
    if (currentQuestionIndex >= questions.size) {
        LaunchedEffect(Unit) {
            viewModel.completeQuiz()
            onComplete()
        }
        return
    }

    val currentQuestion = questions[currentQuestionIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                style = MaterialTheme.typography.labelLarge,
                color = Purple40
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = currentQuestion.text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column {
            currentQuestion.options.forEach { option ->
                OutlinedButton(
                    onClick = {
                        if (option == currentQuestion.correctAnswer) {
                            viewModel.score += 10
                        }
                        currentQuestionIndex++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    border = BorderStroke(1.dp, Purple40)
                ) {
                    Text(
                        text = option,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { onComplete() }) {
                Text("Exit Quiz")
            }
        }
    }
}