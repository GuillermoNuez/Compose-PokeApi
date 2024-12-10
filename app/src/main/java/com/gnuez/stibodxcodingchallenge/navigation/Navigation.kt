package com.gnuez.stibodxcodingchallenge.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gnuez.stibodxcodingchallenge.util.Constants.LIST_SCREEN
import com.gnuez.stibodxcodingchallenge.viewModel.SharedViewModel
import com.gnuez.stibodxcodingchallenge.navigation.destinations.listComposable
import com.gnuez.stibodxcodingchallenge.navigation.destinations.detailComposable

@Composable

fun SetupNavigation(navController: NavHostController, sharedViewModel: SharedViewModel) {

    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(navController = navController, startDestination = LIST_SCREEN){
        listComposable(navigateToDetailScreen = screen.detail, sharedViewModel)
        detailComposable (sharedViewModel)
    }
}