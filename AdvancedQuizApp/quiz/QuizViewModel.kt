package com.advancedquiz.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancedquiz.auth.WalletConnectManager
import com.advancedquiz.rewards.NftRewardManager
import com.advancedquiz.quiz.model.ArQuestion
import com.google.ar.sceneform.math.Vector3
import kotlinx.coroutines.launch

class QuizViewModel(
    private val walletConnectManager: WalletConnectManager,
    val nftRewardManager: NftRewardManager
) : ViewModel() {

    var score: Int = 0
    var level: Int = 1
    var showNftReward by mutableStateOf(false)
    var currentNftReward by mutableStateOf<NftReward?>(null)
    var currentArQuestion by mutableStateOf<ArQuestion?>(null)
    val arQuestions = mutableListOf<ArQuestion>()

    fun completeQuiz(isArQuiz: Boolean = false) {
        viewModelScope.launch {
            // Calculate bonus for AR quizzes
            val scoreBonus = if (isArQuiz) 20 else 0
            val totalScore = score + scoreBonus
            
            if (totalScore >= 80) { // Mint NFT for high scores
                val transactionHash = nftRewardManager.mintRewardNft(level, totalScore)
                if (transactionHash != null) {
                    val newNft = nftRewardManager.getOwnedNfts().lastOrNull()
                    currentNftReward = newNft
                    showNftReward = true
                }
            }
        }
    }

    fun loadArQuestions() {
        viewModelScope.launch {
            // Example AR questions
            arQuestions.addAll(listOf(
                ArQuestion.FloatingTextQuestion(
                    id = "ar1",
                    text = "What's the capital of France?",
                    options = listOf("London", "Paris", "Berlin", "Madrid"),
                    correctAnswer = "Paris",
                    position = Vector3(0f, 0f, -1f)
                ),
                ArQuestion.ObjectSelectionQuestion(
                    id = "ar2",
                    text = "Select the Eiffel Tower",
                    options = listOf("Model1", "Model2", "Model3", "Model4"),
                    correctAnswer = "Model2",
                    position = Vector3(0f, 0f, -1.5f),
                    modelResId = R.raw.ar_models,
                    optionsPositions = listOf(
                        Vector3(-0.5f, 0f, -1f),
                        Vector3(-0.5f, 0f, -1.5f),
                        Vector3(0.5f, 0f, -1f),
                        Vector3(0.5f, 0f, -1.5f)
                    )
                )
            ))
            currentArQuestion = arQuestions.firstOrNull()
        }
    }

    fun answerArQuestion(selectedAnswer: String): Boolean {
        currentArQuestion?.let { question ->
            val isCorrect = question.correctAnswer == selectedAnswer
            if (isCorrect) {
                score += 10 // Bonus points for AR questions
                currentArQuestion = arQuestions.getOrNull(arQuestions.indexOf(question) + 1)
            }
            return isCorrect
        }
        return false
    }

    fun dismissNftReward() {
        showNftReward = false
        currentNftReward = null
    }
}