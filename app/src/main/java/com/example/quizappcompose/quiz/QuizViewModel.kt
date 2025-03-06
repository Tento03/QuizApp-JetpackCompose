package com.example.quizappcompose.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizappcompose.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private var repository: QuizRepository):ViewModel() {

    private val _getQuiz = MutableStateFlow<List<Result>>(emptyList())
    var getQuiz:MutableStateFlow<List<Result>> = _getQuiz

    private val _getIncorectAnswers=MutableStateFlow<List<String>>(emptyList())
    var getIncorectAnswers:MutableStateFlow<List<String>> =_getIncorectAnswers

    fun getQuiz(amount: String, difficulty:String, type:String) {
        try {
            viewModelScope.launch {
                val response=repository.getQuiz(amount,difficulty,type).results
                _getQuiz.value=response

                val incorrect=response.flatMap {
                    it.incorrect_answers
                }
                _getIncorectAnswers.value=incorrect
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

}