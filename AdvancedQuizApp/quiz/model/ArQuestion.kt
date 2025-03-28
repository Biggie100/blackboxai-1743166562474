package com.advancedquiz.quiz.model

import com.google.ar.sceneform.math.Vector3

sealed class ArQuestion(
    open val id: String,
    open val text: String,
    open val options: List<String>,
    open val correctAnswer: String,
    open val position: Vector3 = Vector3(0f, 0f, 0f)
) {
    data class FloatingTextQuestion(
        override val id: String,
        override val text: String,
        override val options: List<String>,
        override val correctAnswer: String,
        override val position: Vector3,
        val textSize: Float = 0.1f
    ) : ArQuestion(id, text, options, correctAnswer, position)

    data class ObjectSelectionQuestion(
        override val id: String,
        override val text: String,
        override val options: List<String>,
        override val correctAnswer: String,
        override val position: Vector3,
        val modelResId: Int,
        val optionsPositions: List<Vector3>
    ) : ArQuestion(id, text, options, correctAnswer, position)

    data class SpatialRelationshipQuestion(
        override val id: String,
        override val text: String,
        override val options: List<String>,
        override val correctAnswer: String,
        override val position: Vector3,
        val models: Map<String, Int>, // Map of model names to resource IDs
        val correctArrangement: List<String>
    ) : ArQuestion(id, text, options, correctAnswer, position)
}