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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoQuizScreen(innerPadding: Modifier) {
    val purpleColor = Color(0xFF6200EE)

    var currentQuestionIndex by remember { mutableStateOf(0) }
    val currentQuestion = questionList[currentQuestionIndex]
    var isAnswered by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableStateOf(0) }

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

            Spacer(modifier = Modifier.height(24.dp))       // ← ПЕРЕНЕСЕНО

            if (isAnswered == false) {                      // ← ПЕРЕНЕСЕНО
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val isCorrect = checkAnswer(true, currentQuestion.correctAnswer)
                            if (isCorrect) {
                                correctAnswersCount = correctAnswersCount + 1
                            }
                            isAnswered = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = purpleColor)
                    ) {
                        Text(text = "TRUE")
                    }

                    Button(
                        onClick = {
                            val isCorrect = checkAnswer(false, currentQuestion.correctAnswer)
                            if (isCorrect) {
                                correctAnswersCount = correctAnswersCount + 1
                            }
                            isAnswered = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = purpleColor)
                    ) {
                        Text(text = "FALSE")
                    }
                }
            }
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