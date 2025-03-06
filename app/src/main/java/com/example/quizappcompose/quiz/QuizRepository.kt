package com.example.quizappcompose.quiz

import com.example.quizappcompose.api.QuizApi
import com.example.quizappcompose.model.QuizResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(private var quizApi: QuizApi) {
    suspend fun getQuiz(amount: String, difficulty:String, type:String):QuizResponse{
        return quizApi.getQuiz(amount,difficulty,type)
    }
}