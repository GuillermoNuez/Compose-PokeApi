package com.gnuez.stibodxcodingchallenge.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gnuez.stibodxcodingchallenge.activities.ListScreen
import com.gnuez.stibodxcodingchallenge.data.models.PokemonData
import com.gnuez.stibodxcodingchallenge.util.Constants.LIST_SCREEN
import com.gnuez.stibodxcodingchallenge.viewModel.SharedViewModel

fun NavGraphBuilder.listComposable(
    navigateToDetailScreen: (pokemonName: String) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN
    ) {
        ListScreen(navigateToDetailScreen, sharedViewModel)
    }
}