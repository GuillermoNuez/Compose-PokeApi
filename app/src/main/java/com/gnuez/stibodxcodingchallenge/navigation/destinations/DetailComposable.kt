package com.gnuez.stibodxcodingchallenge.navigation.destinations

import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gnuez.stibodxcodingchallenge.activities.DetailScreen
import com.gnuez.stibodxcodingchallenge.util.Constants.DETAIL_ARGUMENT_KEY
import com.gnuez.stibodxcodingchallenge.util.Constants.DETAIL_SCREEN
import com.gnuez.stibodxcodingchallenge.viewModel.SharedViewModel


fun NavGraphBuilder.detailComposable(
    sharedViewModel: SharedViewModel
) {
    composable(
        route = DETAIL_SCREEN,
        arguments = listOf(navArgument(DETAIL_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) { navBackStackEntry ->


        val pokemonData by sharedViewModel.pokemonDetails.collectAsState()

        val pokemonName = navBackStackEntry.arguments!!.getString(DETAIL_ARGUMENT_KEY)

        sharedViewModel.fetchPokemonDetails(pokemonName!!)

        if (pokemonData != null) {
            sharedViewModel.checkIfFav(pokemonData!!.id)

            DetailScreen(pokemonData!!, sharedViewModel, LocalContext.current)

        } else {

            Text("something went wrong: " + pokemonName)
        }


    }
}