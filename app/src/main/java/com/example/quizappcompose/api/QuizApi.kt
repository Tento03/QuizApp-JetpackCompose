package com.example.quizappcompose.api

import com.example.quizappcompose.model.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface QuizApi {
    companion object{
        const val BASE_URL="https://opentdb.com/"
    }

    @Headers("Cache-Control: max-age=60")
    @GET("api.php")
    suspend fun getQuiz(
        @Query("amount") amount: String,
        @Query("difficulty") difficulty:String?,
        @Query("type") type:String?
    ):QuizResponse
}