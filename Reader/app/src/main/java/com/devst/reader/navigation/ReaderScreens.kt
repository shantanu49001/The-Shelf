package com.devst.reader.navigation

//pehle screens physcally lelo
//fir enum me typos name
enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    ReaderHomeScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    ReaderStatsScreen;

    companion object {
        fun fromRoute(route:String):ReaderScreens=
            when(route?.substringBefore("/")){
                //these are enum typi
                SplashScreen.name->SplashScreen
                LoginScreen.name->LoginScreen  //thse are actual screen
                CreateAccountScreen.name->CreateAccountScreen
                ReaderHomeScreen.name->ReaderHomeScreen
                SearchScreen.name->SearchScreen
                DetailScreen.name->DetailScreen
                UpdateScreen.name->UpdateScreen
                ReaderHomeScreen.name->ReaderHomeScreen
                null->ReaderHomeScreen
                else -> throw java.lang.IllegalArgumentException("Route $route is not found")
            }
    }
    //iske baad navhost and navgraph


}