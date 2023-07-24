package com.devst.reader.Screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devst.reader.components.ReaderLogo
import com.devst.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

//@Preview  //justadd a dummy nvcotrioller param value
@Composable
fun ReaderSplashScreen(navController: NavHostController) {

    //  Text(text = "SplashScreen")

    //curcle for  k rhe hai
    //we want to animate this size ->state size animator function bith required 

    val scale = remember {  //state var of type animatable
        androidx.compose.animation.core.Animatable(0f)
    }

    //bg me function iski value chage krta rahega ->apne aap minitor higu
    LaunchedEffect(key1 = true) {
        //bg me hai
        scale.animateTo(targetValue = 0.9f,
            animationSpec = tween(durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            )
        )
        //crotuine paused hai mat  thread nhi
        delay(2000L)
        //thread me hi delay ke baad

        //if user already loggeDin  has data in db
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrBlank()) {  //also we can chekc if signed In asn imolment signput button
            navController.navigate(ReaderScreens.LoginScreen.name)
        }

        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
     //     navController.navigate(ReaderScreens.LoginScreen.name)
    }


    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(
            width = 2.dp,
            color = Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //just wanted to reuse it
            ReaderLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "\"Read.Change.Eligten\"",
                style = MaterialTheme.typography.h5, color = Color.LightGray
            )
        }
    }
}

