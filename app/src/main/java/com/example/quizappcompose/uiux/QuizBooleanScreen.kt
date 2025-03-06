package com.example.quizappcompose.uiux

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizappcompose.model.Quiz
import com.example.quizappcompose.quiz.QuizViewModel
import com.google.gson.Gson

@Composable
fun QuizBooleanScreen(
    quizJson: String,
    viewModel: QuizViewModel = hiltViewModel(),
    category: String = "",
    amount: Int = 1,
    difficulty: String = "easy",
    type: String = "boolean"
) {
    val quiz = Gson().fromJson(quizJson, Quiz::class.java)

    LaunchedEffect(Unit) {
        viewModel.getQuiz(quiz.amount, quiz.difficulty, quiz.type)
    }

    val quizData by viewModel.getQuiz.collectAsState()
    var userAnswers by rememberSaveable { mutableStateOf<Map<String, String>>(emptyMap()) }

    var correctAnswersCount by rememberSaveable { mutableStateOf(0) }

    var showScore by remember {
        mutableStateOf(false)
    }
    // Tampilan daftar pertanyaan dengan LazyColumn
    LazyColumn(
        modifier = Modifier
            .padding(top = 50.dp, start = 20.dp, end = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quizData) { item ->
            QuizBooleanCard(
                listItem = listOf("True", "False"),
                question = item.question,
                correctAnswers = item.correct_answer,
                count = quiz.amount.toInt(),
                onAnswerSelected = { selectedAnswer ->
                    userAnswers = userAnswers + (item.question to selectedAnswer)
                    correctAnswersCount = userAnswers.count { (question, answer) ->
                        answer == quizData.find { it.question == question }?.correct_answer
                    }
                },
                isAnswerSelected = userAnswers[item.question] // Menjaga status pilihan
            )
            Button(onClick = {showScore=true}, enabled = userAnswers.size==quizData.size) {
                Text("Show")
            }
        }
    }

    // Menampilkan skor final jika semua pertanyaan sudah dijawab
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showScore){
            Text(
                "Final Score: $correctAnswersCount / ${quizData.size}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuizBooleanCard(
    listItem: List<String>,
    question: String,
    correctAnswers: String,
    count: Int,
    onAnswerSelected: (String) -> Unit,
    isAnswerSelected: String?
) {
    var isSelected by remember { mutableStateOf(isAnswerSelected) }

    // Mengatur warna kartu berdasarkan jawaban
    val colorCards = when {
        isSelected == null -> MaterialTheme.colorScheme.surface
        isSelected == correctAnswers -> Color.Green
        else -> Color.Red
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorCards),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                question,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify
            )
            Column {
                listItem.forEach { item ->
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected == item,
                            onClick = {
                                isSelected = item
                                onAnswerSelected(item) // Mengirim jawaban yang dipilih
                            }
                        )
                        Text(item)
                    }
                }
            }
        }
    }
}
