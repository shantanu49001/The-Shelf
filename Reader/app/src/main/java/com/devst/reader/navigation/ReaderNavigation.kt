package com.devst.reader.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bawp.freader.screens.update.BookUpdateScreen
import com.devst.reader.Screens.ReaderSplashScreen
import com.devst.reader.Screens.details.BookDetailsScreen
import com.devst.reader.Screens.home.Home
import com.devst.reader.Screens.home.HomeScreenViewModel
import com.devst.reader.Screens.login.ReaderLoginScreen
import com.devst.reader.Screens.search.BookSearchViewModel
import com.devst.reader.Screens.search.SearchScreen
import com.devst.reader.Screens.stats.ReaderStatsScreen
//import com.devst.reader.Screens.update.BookUpdateScreen

//import com.devst.reader.Screens.update.ReaderLoginScreen


@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()   //remeber as it knows is't curren state

    //main whole app nviagtion routes and working 
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {

        //ye ek rotte hai  bas ek route hai jo leke jaata hai
        //this gives the ebnums the power to do task
        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name) {

            val homeViewModel= hiltViewModel<HomeScreenViewModel>()
            Home(navController,viewModel=homeViewModel)//reuires viewmodel instantiation
        }

        //jab ye call ho to ye karba
        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController)
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {

           val homeViewModel= hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController = navController, viewModel=homeViewModel)
        }
        composable(ReaderScreens.SearchScreen.name) {

            val viewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController = navController, viewModel)
        }


        val detailsNmae =
            ReaderScreens.DetailScreen.name  //argumen send krne ke liye poore path ko string me chage krna padega
        composable("$detailsNmae/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())

            }
            //paticula ritem expand
        }

        val updateName=ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}", arguments = listOf(navArgument("bookItemId"){
            type = NavType.StringType
        })){backStack->
            backStack.arguments?.getString("bookItemId").let {
                //RedBookUpdateScreen(navController = navController, bookItemId = it.toString())
            BookUpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }
    }


}