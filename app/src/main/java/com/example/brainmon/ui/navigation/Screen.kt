package com.example.brainmon.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")

    object Explore : Screen("explore_screen")



    object Lab : Screen("lab/{monsterId}") {

        fun createRoute(monsterId: String) = "lab/$monsterId"
    }

    object Battle : Screen("battle/{biome}") {
        fun createRoute(biome: String) = "battle/$biome"
    }
}