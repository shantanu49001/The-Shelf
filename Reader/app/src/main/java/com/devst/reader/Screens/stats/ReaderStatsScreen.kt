package com.devst.reader.Screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.devst.reader.Screens.home.HomeScreenViewModel
import com.devst.reader.components.ReaderAppBar
import com.devst.reader.model.Item
import com.devst.reader.model.MBook
import com.devst.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderStatsScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Your Stats",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {
        Surface() {
            //only sjow books that user read
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    //is user ki boos
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()
            }

            Column {
                Row(
                    modifier = Modifier
                        .size(45.dp)
                        .padding(2.dp)
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "")

                }
                Text(text = "Hi! ${currentUser?.email.toString().split("@")[0]}")

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), shape = CircleShape, elevation = 5.dp
                ) {
                    val readBooksList: List<MBook> =
                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                            books.filter { mBook ->
                                mBook.userId == currentUser?.uid && mBook.finishedReading != null
                            }
                        } else {
                            emptyList()
                        }
                    val readingBooks = books.filter { mBook ->
                        mBook.startedReading != null && mBook.finishedReading == null

                    }
                    Column(
                        modifier = Modifier.padding(start = 25.dp, top = 5.dp, bottom = 5.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "Your Stats", style = MaterialTheme.typography.h5)
                        Divider()
                        Text(text = "You're reading : ${readingBooks.size} books")
                        Text(text = "You've read: ${readBooksList.size} books")
                    }
                }

                if (viewModel.data.value.loading == true) {
                    LinearProgressIndicator()
                } else {
                    Divider()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(), contentPadding = PaddingValues(16.dp)
                    ) {

                        val readingList: List<MBook> =
                            if (!viewModel.data.value.data.isNullOrEmpty()) {
                                viewModel.data.value.data!!.filter { mbook ->
                                    (mbook.userId == currentUser?.uid) && mbook.finishedReading != null
                                }
                            } else {
                                emptyList()
                            }
                        items(items = readingList) { book ->
                            BookRowStats(book = book)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookRowStats(
    //book: MBook,
    book: MBook,
    //  navController: NavController
) {
    Card(
        modifier = Modifier
            .clickable {
                //    navController.navigate(ReaderScreens.DetailScreen.name)    //id is from data classes
                //   navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp
    ) {

        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {

            val imageUrl: String =
                if (book.photoUrl.toString().isEmpty()) {
                    "https://qph.fs.quoracdn.net/main-qimg-b0b47666b251ec315fbe0e8a1ba9d6bd-c"
                } else {
                    book.photoUrl.toString()
                }
            Image(
                painter = rememberImagePainter(
                    data = imageUrl
                    //"https://qph.fs.quoracdn.net/main-qimg-b0b47666b251ec315fbe0e8a1ba9d6bd-c"
                ), contentDescription = "",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)

            )
            Column() {

              Row(horizontalArrangement = Arrangement.SpaceBetween) {
                  Text(
                      text =
                      // book.title.toString(),-->replac if api deon't work
                      book.title.toString(),
                      overflow = TextOverflow.Ellipsis
                  )

                  if (book.rating!!>=4){
                   Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                   Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "", tint = Color.Green.copy(alpha = 0.5f))

                  }else{
                      Box {

                      }
                  }
              }

                Text(
                    //repace if api doen't work
                    //  text = "Authors: ${book.authors.toString()}",
                    text = "Authors:${book.authors}",
                    overflow = TextOverflow.Clip

                )


            }
        }
    }
}