package com.devst.reader

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devst.reader.navigation.ReaderNavigation
import com.devst.reader.ui.theme.ReaderTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

//mai widget me navcontroller hoga jisme start detinatiin already set hai and fir uske baad
//wo internally use krta rahega

//imlment functionalities on the go
//preffered is to imlment login first so that uuid can be used to make uder's collection in the db


//imrovements:  current reading,some updates crashing
//github upload , playstore upload
//note dowm
//machine learning apply


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReaderTheme {
                // A surface container using the 'background' color from the theme

                //ddb structure ke liye sample element add krke dekhlo first before auth etc
//                val db= FirebaseFirestore.getInstance()
//                val user:MutableMap<String,Any> =HashMap()
//                user["firstName"]="Jeo"
//                user["lastName"]="James"
//


//                db.collection("users")  //is collection me ye user map add hogya
//                    .add(user)
//                    .addOnSuccessListener {
//                        Log.d("FB","onCreater: ${it.id}")
//                    }

                ReaderApp()

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ReaderApp() {
    Surface(   //main app starts from here
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp),
        color = MaterialTheme.colors.background
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ReaderNavigation()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReaderTheme {
        //  Greeting("Android")
    }
}