package com.devst.reader.Screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.devst.reader.components.ReaderAppBar
import com.devst.reader.components.RoundedButton
import com.devst.reader.data.Resource
import com.devst.reader.model.Item
import com.devst.reader.model.MBook
import com.devst.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "ProduceStateDoesNotAssignValue")
@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewModel: DeatilsViewModel = hiltViewModel()
) {

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search Books",
            icon = Icons.Default.ArrowBack,
            navController = navController
        ) {
            navController.popBackStack()  //current screen ko hi remove krta ahu navigation pr koi screen add nhi krega y
        }
    }) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //main safe   prefeered to use vm scope in vm only
                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId)
                }.value  //vm me vmscpe nhi use kiya
                //    val data=viewModel.getBookInfo(bookId)

                if (bookInfo.data == null) {
                    Row() {
                        LinearProgressIndicator()
                        Text(text = "Loading...")

                    }
                } else {

                    ShowDeatils(bookInfo, navController)
                    //    Text(text = "Book Description ${bookInfo.data.toString()}")

                }


            }
        }
    }


}

@Composable
fun ShowDeatils(bookInfo: Resource<Item>, navController: NavController) {

    val bookData =
        bookInfo.data?.volumeInfo     //koi dao nhi hai we are just taking it out from api data classes
    val googleBookId = bookInfo.data?.id

    Card(modifier = Modifier.padding(34.dp), shape = CircleShape, elevation = 4.dp) {
        Image(
            painter = rememberImagePainter(data = bookData?.imageLinks?.thumbnail.toString()),
            contentDescription = "", modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp)
        )


    }

    Text(
        text = bookData!!.title, style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2
    )

    Text(text = "Authors: ${bookData?.authors.toString()}")
    Text(text = "Page Count ${bookData?.pageCount.toString()}")
    Text(
        text = "Categories${bookData?.categories.toString()}",
        style = MaterialTheme.typography.subtitle1, maxLines = 3
    )
    Text(
        text = "Published ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.subtitle1
    )

    Spacer(modifier = Modifier.height(5.dp))


    //deription is dynamic
    val cleanDescription =
        HtmlCompat.fromHtml(bookData!!.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

    //catainer that olds dsecritopm
    val localDims = LocalContext.current.resources.displayMetrics   //grow in size
    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(width = 1.dp, color = Color.Gray)
    ) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                Text(text = cleanDescription.toString())
            }
        }
    }

    //buttons
    Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.SpaceAround) {
        RoundedButton(label = "Save") {
            //save to firestore
            val db = FirebaseFirestore.getInstance()   //just project webste link no tools...
            val book = MBook(
                title = bookData.title.toString(),
                authors = bookData.authors.toString(),
                description = bookData.description,
                categories = bookData.categories.toString(),
                notes = "",
                photoUrl = bookData.imageLinks.smallThumbnail,
                publishedDate = bookData.publishedDate,
                pageCount = bookData.pageCount.toString(),
                rating = 0.0,
                googleBookId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            )
            saveToFirebase(book, navController)   //saving books that we inyercated with
        }

        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(label = "Cancel") {

        }


    }

}

fun saveToFirebase(book: MBook, navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")  //poora map of book add
    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { document ->  //sucess data /auth ref return
                val docId = document.id  //upadte the map
                dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->  //task ref return
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }

                    }.addOnFailureListener {
                        Log.w("Sve to fb", "Error upadtin doc ")
                    }
            }
    } else {

    }

}
