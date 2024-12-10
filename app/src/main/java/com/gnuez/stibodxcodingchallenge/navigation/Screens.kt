package com.gnuez.stibodxcodingchallenge.navigation

import androidx.navigation.NavController
import com.gnuez.stibodxcodingchallenge.util.Constants.LIST_SCREEN

class Screens( navController: NavController) {
    val list: () -> Unit = {
        navController.navigate("list/") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }

    val detail: (String) -> Unit = { Id ->
        navController.navigate("detail/$Id") {
        }
    }
}