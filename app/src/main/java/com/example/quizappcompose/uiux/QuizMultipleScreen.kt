package com.example.quizappcompose.uiux

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import java.util.Random

@Composable
fun QuizMultipleScreen(
    quizJson: String,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val quizState by remember { mutableStateOf(Unit) }
    val quizData by viewModel.getQuiz.collectAsState()
    val incorrectAnswers by viewModel.getIncorectAnswers.collectAsState()

    // Menggunakan rememberSaveable agar data tetap tersimpan meskipun ada perubahan konfigurasi
    var userAnswers by rememberSaveable { mutableStateOf<Map<String, String>>(emptyMap()) }
    var correctAnswersCount by rememberSaveable { mutableStateOf(0) }

    val quiz = Gson().fromJson(quizJson, Quiz::class.java)
    var showScore by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(quizState) {
        viewModel.getQuiz(quiz.amount, quiz.difficulty, quiz.type)
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 20.dp, top = 50.dp, end = 20.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quizData) { item ->
            val option = (incorrectAnswers + item.correct_answer).shuffled(random = Random())
            QuizMultipleCard(
                option = option,
                question = item.question,
                correctAnswers = item.correct_answer,
                count = quiz.amount.toInt(),
                onAnswerSelected = { selectedAnswer ->
                    userAnswers = userAnswers + (item.question to selectedAnswer)

                    // Hitung ulang jumlah jawaban benar setiap kali jawaban dipilih
                    correctAnswersCount = userAnswers.count { (question, answer) ->
                        answer == quizData.find { it.question == question }?.correct_answer
                    }
                },
                isAnswerSelected = userAnswers[item.question]
            )
            Button(onClick = {showScore=true}, enabled = userAnswers.size==quizData.size) {
                Text("Show")
            }
        }
    }

    // Tetap tampilkan skor meskipun LazyColumn muncul atau di-scroll
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
private fun QuizMultipleCard(
    option: List<String>,
    question: String,
    correctAnswers: String,
    count: Int,
    onAnswerSelected: (String) -> Unit,
    isAnswerSelected: String?
) {
    var isSelected by remember { mutableStateOf(isAnswerSelected) }

    // Menentukan warna kartu berdasarkan apakah jawaban benar atau salah
    val cardColor = when {
        isSelected == null -> MaterialTheme.colorScheme.surface
        isSelected == correctAnswers -> Color.Green
        else -> Color.Red
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                question,
                fontSize = 20.sp,
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold
            )
            Column {
                option.forEach { item ->
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected == item,
                            onClick = {
                                isSelected = item
                                onAnswerSelected(item)
                            }
                        )
                        Text(item)
                    }
                }
            }
        }
    }
}
