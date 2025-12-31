package com.example.brainmon.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.brainmon.ui.navigation.Screen
import com.example.brainmon.ui.screens.*

object Routes {
    const val Dashboard = "dashboard"
    const val Pokedex = "pokedex"
    const val Stats = "stats"
}
@Composable
fun MonsterApp(
    navController: NavHostController = rememberNavController(),
    viewModel: MonstersViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Dashboard
    ) {
// 2. DASHBOARD
        composable(route = Routes.Dashboard) {
            DashboardScreen(
                onPlayClick = { navController.navigate(Screen.Explore.route) },
                onPokedexClick = { navController.navigate(Routes.Pokedex) },
                onStatsClick = { navController.navigate(Routes.Stats) }
            )
        }

        // 3. POKEDEX (Renamed)
        composable(route = Routes.Pokedex) {
            PokedexScreen(
                viewModel = viewModel,
                onMonsterClick = { id -> navController.navigate(Screen.Lab.createRoute(id)) },
                onAddMonsterClicked = { navController.navigate(Screen.Explore.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 3. EXPLORE (The Map)
        composable(route = Screen.Explore.route) {
            ExploreScreen(
                viewModel = viewModel,
                onBiomeSelected = { biome -> navController.navigate(Screen.Battle.createRoute(biome)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // 4. BATTLE
        composable(
            route = Screen.Battle.route,
            arguments = listOf(navArgument("biome") { type = NavType.StringType })
        ) { entry ->
            val biome = entry.arguments?.getString("biome") ?: "grass"
            LaunchedEffect(biome) { viewModel.startEncounter(biome) }

            BattleScreen(
                viewModel = viewModel,
                onCatchClicked = {
                    navController.popBackStack()
                }
            )
        }

        // 5. LAB
        composable(
            route = Screen.Lab.route,
            arguments = listOf(navArgument("monsterId") { type = NavType.StringType })
        ) { entry ->
            LabScreen(
                monsterId = entry.arguments?.getString("monsterId"),
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Routes.Stats) {
            StatsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}