@file:Suppress("UNREACHABLE_CODE")

package com.example.quizappcompose.uiux

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizappcompose.model.Quiz
import com.example.quizappcompose.quiz.QuizViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizCreateScreen(navController: NavController,viewModel: QuizViewModel= hiltViewModel()) {
    val getQuiz by viewModel.getQuiz.collectAsState()

//    category
    val listCategory = listOf("General Knowledge" to "9","Sports" to "21",
        "History" to "23","Politics" to "24","Art" to "25")

//    difficulty
    val listDifficulty = listOf("easy" to "Easy","medium" to "Medium","hard" to "Hard")

//    type
    val listType = listOf("multiple" to "Multiple Choice","boolean" to "True/False")

    var amount by remember {
        mutableStateOf("1")
    }
    var category by remember {
        mutableStateOf(listCategory[0])
    }
    var difficulty by remember {
        mutableStateOf(listDifficulty[0])
    }
    var type by remember {
        mutableStateOf(listType[0])
    }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedDifficulty by remember { mutableStateOf(false) }
    var expandedType by remember { mutableStateOf(false) }

    val keyboardController= LocalSoftwareKeyboardController.current
    val context= LocalContext.current

    Column (modifier = Modifier.fillMaxSize().padding(top = 100.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount=it
            },
            placeholder = { Text("Amount of Questions?") },
            label = { Text("Question") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenuBox(
            expanded = expandedDifficulty,
            onExpandedChange = {
                expandedDifficulty=!expandedDifficulty
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = difficulty.second,
                onValueChange = {},
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expandedDifficulty)}
            )
            ExposedDropdownMenu(
                expanded = expandedDifficulty,
                onDismissRequest = {expandedDifficulty=false},
                modifier = Modifier.fillMaxWidth()
            ) {
                listDifficulty.forEach { diffItem->
                    DropdownMenuItem(
                        text = { Text(diffItem.second) } ,
                        onClick = {difficulty=diffItem
                            expandedDifficulty=false
                                  },
                    )
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = expandedType,
            onExpandedChange = {
                expandedType=!expandedType
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = type.second,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expandedType)}
            )
            ExposedDropdownMenu(
                expanded = expandedType,
                onDismissRequest = {expandedType=false},
                modifier = Modifier.fillMaxWidth(),
            ) {
                listType.forEach { listType->
                    DropdownMenuItem(
                        text = { Text(listType.second) },
                        onClick = {
                            type=listType
                            expandedType=false
                        },
                    )
                }
            }
        }
        Button(onClick = {
            if (type.first=="multiple"){
                viewModel.getQuiz(amount,difficulty.first, type.first)
                Toast.makeText(context,"Request senter",Toast.LENGTH_SHORT).show()
                val quiz=Quiz(amount,type.first,difficulty.first)
                val quizJson=Gson().toJson(quiz)
                val uriEncoded=Uri.encode(quizJson)
                println("Amount:$amount,type:${type.first},diff:${difficulty.first}")
                navController.navigate("Multiple/$uriEncoded")
                amount=""
            }
            else{
                viewModel.getQuiz(amount,difficulty.first, type.first)
                Toast.makeText(context,"Request sent",Toast.LENGTH_SHORT).show()
                val quiz=Quiz(amount,type.first,difficulty.first)
                val quizJson=Gson().toJson(quiz)
                val uriEncoded=Uri.encode(quizJson)
                println("Amount:$amount,type:${type.first},diff:${difficulty.first}")
                navController.navigate("Boolean/$uriEncoded")
                amount=""
            }
        }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)) {
            Text("Submit")
        }
    }
//    QuizMultipleCard(
//        listItem = tabList,
//        isSelected= isSelected
//    )
}

