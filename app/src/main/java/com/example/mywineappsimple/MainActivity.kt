package com.example.mywineappsimple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mywineappsimple.ui.detail.DetailScreen
import com.example.mywineappsimple.ui.home.HomeScreen
import com.example.mywineappsimple.ui.input.InputScreen
import com.example.mywineappsimple.ui.theme.MyWineAppSimpleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWineAppSimpleTheme {
                Surface {
                    MyWineApp()
                }
            }
        }
    }
}

@Composable
fun MyWineApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            HomeScreen(navController)
        }
        composable(route = "input") {
            InputScreen(navController)
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            } )
        ) {
            DetailScreen(navController)
        }
    }
}
