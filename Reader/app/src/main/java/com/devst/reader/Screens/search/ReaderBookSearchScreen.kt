package com.devst.reader.Screens.search

import android.annotation.SuppressLint
import android.graphics.fonts.FontStyle
import android.renderscript.ScriptGroup.Input
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.devst.reader.components.InputField
import com.devst.reader.components.ReaderAppBar
import com.devst.reader.model.Item
import com.devst.reader.model.MBook
import com.devst.reader.navigation.ReaderScreens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable

fun SearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = hiltViewModel()

) {
    Scaffold(
        topBar = { //we will use this at many plaves
            ReaderAppBar(
                title = "Search Books",
                icon = Icons.Default.ArrowBack,
                navController = navController
            ) {
                navController.popBackStack()  //current screen ko hi remove krta ahu navigation pr koi screen add nhi krega y
            }
        }
    ) {


        Surface() {
            Column {

                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    viewModel = viewModel
                ) { query ->
                    //onsearch function  event se string ishar acessble hai
                    viewModel.searchBooks(query = query)


                }
                Spacer(modifier = Modifier.height(13.dp))
                BookList(navController, viewModel)
            }
        }


    }
}

@Composable
fun BookList(navController: NavController, viewModel: BookSearchViewModel = hiltViewModel()) {
//recyler view  only vertical ; lasly we used for loop to genreate where thary were horxtal scroll view

    //replace ths to show content s if api returns error
    val listOfBooks = viewModel.list
    //   val listOfBooks = listOf<MBook>(
//        MBook("dada", title = "Hello Again", "All of us", notes = null),
//        MBook("dada", title = "Hello Again", "All of us", notes = null),
//        MBook("dada", title = "Hello Again", "All of us", notes = null),
//        MBook("dada", title = "Hello Again", "All of us", notes = null)
//    )

    if (viewModel.isLoading) {
        LinearProgressIndicator()  //viewmodel ka var data load hone ke baad reset uski track se idhar shimmer effect show
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {

            items(items = listOfBooks) { book ->
                BookRow(book, navController)
            }
        }
    }


}

@Composable
fun BookRow(
    //book: MBook,
    book: Item,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .clickable {
                //    navController.navigate(ReaderScreens.DetailScreen.name)    //id is from data classes
                navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp
    ) {

        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {

            val imageUrl: String =
                if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty() == true) {
                    "https://qph.fs.quoracdn.net/main-qimg-b0b47666b251ec315fbe0e8a1ba9d6bd-c"
                } else {
                    book.volumeInfo.imageLinks.smallThumbnail
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

                Text(
                    text =
                    // book.title.toString(),-->replac if api deon't work
                    book.volumeInfo.title,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    //repace if api doen't work
                    //  text = "Authors: ${book.authors.toString()}",
                    text = "Authors:${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip

                )


            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {

    Column() {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }

        val keyBoardController = LocalSoftwareKeyboardController.current

        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }
        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyBoardController?.hide()
            })
    }
}
