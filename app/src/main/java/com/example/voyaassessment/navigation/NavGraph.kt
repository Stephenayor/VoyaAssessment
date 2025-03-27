package com.example.voyaassessment.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voyaassessment.presentation.home.FoodHomeScreen
import com.example.voyaassessment.presentation.details.CreateFoodDetailsScreen
import com.example.voyaassessment.presentation.update.FoodDetailsScreen
import com.example.voyaassessment.utils.Route

@Composable
fun Navigation(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.FOOD_HOME_SCREEN) {
        composable(Route.FOOD_HOME_SCREEN){
            FoodHomeScreen(modifier, navController)
        }

        composable(Route.CREATE_FOOD_DETAILS_SCREEN){
            CreateFoodDetailsScreen(modifier,  navController)
        }

        composable("foodDetailsScreen/{foodId}") { backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId")
            Log.d("NavGraph", foodId.toString())
            foodId?.let { FoodDetailsScreen(foodId = it, navController = navController) }
        }

    }

}