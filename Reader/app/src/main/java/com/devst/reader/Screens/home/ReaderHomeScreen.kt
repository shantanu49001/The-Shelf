package com.devst.reader.Screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.devst.reader.components.*
import com.devst.reader.model.MBook
import com.devst.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

//floating action button scaffold ke saath hi ati hai

//if we are suign image links that are ni secire go to manifest usesClearTextTraffic true
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavHostController, viewModel: HomeScreenViewModel = hiltViewModel()) {

    Scaffold(
        topBar = { //we will use this at many plaves
            ReaderAppBar(title = "Shelf By DevST", navController = navController)
        },
        floatingActionButton = {
            FABContent {//param is labda
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) {
        //conent
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeContent(navController, viewModel)
        }
    }

}

//this will also use the routes
@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel) {
//
//    val listOfBooks = listOf<MBook>(
//        MBook("dada", title = "Hello Again", "All of us", notes = null),
//        MBook("dada", title = "Hello Again", "All of us", notes = null),
//        MBook("dada", title = "Hello Again", "All of us", notes = null),
//        MBook("dada", title = "Hello Again", "All of us", notes = null)
//    )
    var listOfBooks = emptyList<MBook>()  //non ui htingsraeaceesble in scopes
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        //doe in bg viewm scop
        listOfBooks = viewModel.data.value.data!!.toList()!!.filter { book ->
            book.userId == currentUser?.uid.toString()  //fietring through the user added books

        }
        Log.d("Books", "Home Content :${listOfBooks.toString()}")
    }

    val currentUserName = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email
            ?.split("@")?.get(0)
    else "N/A"



    Column(Modifier.padding(2.dp), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your reading\n" + "activity right now..")
            Spacer(modifier = Modifier.fillMaxWidth(0.6f)) //reponsive spaver
            Column {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "profilr",
                    modifier = Modifier
                        .clickable {
                            //take user to his profile stats
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Text(
                    text = currentUserName!!, modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis   //fit correct letter
                )
                Divider()   //name ke neeche line

            }
        }
        ReadingRightNowArear(
            books = listOf(),
            navController = navController
        )
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {

    //fiteing thise books that are notread currently
    val addedBooks = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }

    HorizontalScaroolableComponent(addedBooks) {
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }
}


//scrollable row that remebers last scroll
//viewmodel isdhar dega list not at the widgts and lsit state bhi idhar ho hogi
@Composable
fun HorizontalScaroolableComponent(
    listOfBooks: List<MBook>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    OnCardPressed: (String) -> Unit
) {

    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .horizontalScroll(scrollState)
    ) {

        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator()
        } else {
            if (listOfBooks.isNullOrEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(
                        text = "No Books found ,add books",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
            } else {
                for (book in listOfBooks) {   //loop se widgets crte in row that is scrollable
                    ListCard(book) {   //event present in card
                        //isme param ke poi aur function hai
                        // mai idhar se apna function send

                        OnCardPressed(book.googleBookId.toString())
                    }
                }
            }
        }


    }

}


//we will use this widget agr with diffeent hahpes


@Composable
fun ReadingRightNowArear(books: List<MBook>, navController: NavController) {
    //Filter books by reading now

    val readingNowList = books.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }

    HorizontalScaroolableComponent(listOfBooks = readingNowList) {
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }
}


