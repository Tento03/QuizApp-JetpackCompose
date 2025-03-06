package com.example.quizappcompose

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizappcompose.ui.theme.QuizAppComposeTheme
import com.example.quizappcompose.uiux.QuizBooleanScreen
import com.example.quizappcompose.uiux.QuizCreateScreen
import com.example.quizappcompose.uiux.QuizMultipleScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppComposeTheme {
                val navController= rememberNavController()
                NavHost(navController=navController, startDestination = "Create"){
                    composable(route = "Create"){
                        QuizCreateScreen(navController)
                    }
                    composable(route = "Multiple/{quiz}",
                        arguments = listOf(navArgument("quiz",{
                            type= NavType.StringType
                        }))
                    ){
                        val quiz= it.arguments?.getString("quiz")
                        if (quiz != null) {
                            QuizMultipleScreen(quiz)
                        }
                    }
                    composable(route = "Boolean/{quiz}",
                        arguments = listOf(navArgument("quiz",{
                            type= NavType.StringType
                        }))
                    ){
                        var quiz=it.arguments?.getString("quiz")
                        if (quiz != null) {
                            QuizBooleanScreen(quiz)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizAppComposeTheme {
        Greeting("Android")
    }
}