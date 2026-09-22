package com.example.geoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.geoquiz.ui.theme.GeoquizTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoquizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GeoQuizScreen(innerPadding = Modifier.padding(innerPadding))

                }
            }
        }
    }

    data class Question(val text: String, val correctAnswer: Boolean)

    val questionList = listOf(
        Question("Canberra is the capital of Australia.", true),
        Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
        Question("The Suez Canal connects the Red Sea and the Indian Ocean.", false),
        Question("The source of the Nile River is in Egypt.", false),
        Question("The Amazon River is the longest river in the Americas.", true),
        Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
    )

    fun checkAnswer(userAnswer: Boolean, correctAnswer: Boolean): Boolean {
        return userAnswer == correctAnswer
    }

    fun buildResultMessage(correctCount: Int, totalCount: Int): String {
        return "Правильных ответов: " + correctCount + " из " + totalCount
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GeoQuizScreen(innerPadding: Modifier) {
        val purpleColor = Color(0xFF6200EE)
        val lightPurple = Color(0xFFBB86FC)   // ← СВЕТЛО-ФИОЛЕТОВЫЙ для выбранной

        var currentQuestionIndex by remember { mutableStateOf(0) }
        val currentQuestion = questionList[currentQuestionIndex]
        var isAnswered by remember { mutableStateOf(false) }
        var correctAnswersCount by remember { mutableStateOf(0) }
        val isLastQuestion = currentQuestionIndex == questionList.size - 1

        // Какая кнопка была нажата: "TRUE", "FALSE" или null
        var selectedAnswer by remember { mutableStateOf<String?>(null) }

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = innerPadding.fillMaxSize()) {
            TopAppBar(
                title = { Text(text = "GeoQuiz") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = purpleColor,
                    titleContentColor = Color.White
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = currentQuestion.text,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // TRUE
                    Button(
                        onClick = {
                            if (isAnswered == false) {
                                selectedAnswer = "TRUE"
                                val isCorrect = checkAnswer(true, currentQuestion.correctAnswer)
                                if (isCorrect) correctAnswersCount += 1
                                isAnswered = true

                                if (isLastQuestion) {
                                    val message =
                                        buildResultMessage(correctAnswersCount, questionList.size)
                                    coroutineScope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedAnswer == "TRUE") lightPurple else purpleColor
                        )
                    ) {
                        Text(text = "TRUE")
                    }

                    // FALSE
                    Button(
                        onClick = {
                            if (isAnswered == false) {
                                selectedAnswer = "FALSE"
                                val isCorrect = checkAnswer(false, currentQuestion.correctAnswer)
                                if (isCorrect) correctAnswersCount += 1
                                isAnswered = true

                                if (isLastQuestion) {
                                    val message =
                                        buildResultMessage(correctAnswersCount, questionList.size)
                                    coroutineScope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedAnswer == "FALSE") lightPurple else purpleColor
                        )
                    ) {
                        Text(text = "FALSE")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ===== NEXT =====
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (isAnswered && isLastQuestion == false) {
                                currentQuestionIndex += 1
                                isAnswered = false
                                selectedAnswer = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = purpleColor)
                    ) {
                        Text(text = "NEXT")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = buildResultMessage(correctAnswersCount, questionList.size),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                containerColor = purpleColor,
                contentColor = Color.White
            ) {
                Text(
                    text = data.visuals.message,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GeoQuizScreenPreview() {
        GeoquizTheme {
            GeoQuizScreen(innerPadding = Modifier)
        }
    }
}