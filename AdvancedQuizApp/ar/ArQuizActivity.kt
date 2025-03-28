package com.advancedquiz.ar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment
import com.advancedquiz.quiz.Question
import com.advancedquiz.core.ui.theme.AdvancedQuizTheme

class ArQuizActivity : ComponentActivity() {
    private lateinit var arFragment: ArFragment
    private lateinit var arSession: Session
    private var currentQuestion by mutableStateOf<Question?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AdvancedQuizTheme {
                ArQuizScreen(
                    question = currentQuestion,
                    onAnswerSelected = { answer ->
                        // Handle answer selection
                    }
                )
            }
        }

        initializeAr()
    }

    private fun initializeAr() {
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arSession = Session(this)
        val config = Config(arSession)
        config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
        arSession.configure(config)
    }
}

@Composable
fun ArQuizScreen(
    question: Question?,
    onAnswerSelected: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // AR Fragment container
        AndroidView(
            factory = { context ->
                ArFragment(context).apply {
                    setOnTapArPlaneListener { hitResult, _, _ ->
                        // Place question in AR space
                        placeQuestionInAr(hitResult.createAnchor(), question)
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Floating question UI
        question?.let { q ->
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = q.text,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    q.options.forEach { option ->
                        Button(
                            onClick = { onAnswerSelected(option) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(option)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

private fun placeQuestionInAr(anchor: Anchor, question: Question?) {
    question?.let { q ->
        ModelRenderable.builder()
            .setSource(this, R.raw.question_marker)
            .build()
            .thenAccept { renderable ->
                val anchorNode = AnchorNode(anchor)
                val questionNode = TransformNode().apply {
                    renderable = renderable
                    localScale = Vector3(0.3f, 0.3f, 0.3f)
                }

                val questionText = Node().apply {
                    renderable = ViewRenderable.builder()
                        .setView(this@ArQuizActivity, R.layout.ar_question_view)
                        .build()
                        .get()
                        .apply {
                            (view as TextView).text = q.text
                        }
                    localPosition = Vector3(0f, 0.5f, 0f)
                }

                anchorNode.addChild(questionNode)
                anchorNode.addChild(questionText)
                arFragment.arSceneView.scene.addChild(anchorNode)
            }
            .exceptionally {
                Toast.makeText(this, "Error loading AR model", Toast.LENGTH_SHORT).show()
                null
            }
    }
}
