package com.gnuez.stibodxcodingchallenge.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gnuez.stibodxcodingchallenge.navigation.SetupNavigation
import com.gnuez.stibodxcodingchallenge.ui.theme.PokeApiTheme
import com.gnuez.stibodxcodingchallenge.viewModel.SharedViewModel

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel.loadFavorites(context = this.baseContext)
        enableEdgeToEdge()
        setContent {
            PokeApiTheme {
                navController = rememberNavController()
                SetupNavigation(navController = navController, sharedViewModel)
            }
        }
    }

}