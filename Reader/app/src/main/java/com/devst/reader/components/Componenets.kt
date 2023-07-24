package com.devst.reader.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.devst.reader.R
//import com.devst.reader.Screens.home.RoundedButton
import com.devst.reader.model.MBook
import com.devst.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

//sinmple a designed text
@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(16.dp),
        text = "The Shelf", style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.5f)
    )
}


//text  field inpu t
//yaha se outlinedtextfiedl tak text input ui hai

@Composable
fun emailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default  //jab bhi ispr kuch event higa keyboard aayega
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default

) {

    //now using inbuilt one                       //callback
    OutlinedTextField(
        value = valueState.value, onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        ), //auto coloure that is neessary
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
    )
}


//password compeent
@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisisbilty: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTranformation = if (passwordVisisbilty.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it  //jo vale event se aa rhi hai use assign
        },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTranformation,
        trailingIcon = {
            //widget hai state pass kiya
            PasswordVisibiltity(passwordVisisbilty = passwordVisisbilty)
        },
        keyboardActions = onAction


    )
}

@Composable
fun PasswordVisibiltity(passwordVisisbilty: MutableState<Boolean>) {
    val visible = passwordVisisbilty.value
    IconButton(onClick = {
        passwordVisisbilty.value = !visible
    }) {  //even thai
        Icons.Default.Close
    }
}


@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {  //passed size
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Left
                )
            )
        }
    }
}


@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,  //kisi screen me nhi goha
    showProfile: Boolean = true,
    navController: NavController,   //naviagte rotue dega
    onBackArraowClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                //kisi kisi screen me optional wifgets of appbar
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.Book, contentDescription = "",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(1.0f)  //0.1>0.6
                    )


                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            tint = Color.Red.copy(alpha = 0.7f),
                            modifier = Modifier.clickable { onBackArraowClicked.invoke() })
                    }
                    Spacer(modifier = Modifier.width(40.dp))  //modofiab;e spaveer


                    Text(
                        text = title,
                        color = Color.Red.copy(alpha = 0.7f),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    )

                }
            }
        },
        //app bar's right most buttons come under actions
        actions = {
            IconButton(onClick = {
                //when th work is scuessful and not null returned do this labda
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name)
                }
            }) {
                //all these icons come form material icons dependency

                if (showProfile) Row() {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "",
                        tint = Color.Green.copy(alpha = 0.4f)
                    )
                } else {
                    Box(modifier = Modifier)
                }


            }
        }, backgroundColor = Color.Transparent, elevation = 0.dp
    )


}


@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },  //actual work function called on event of lower widget
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xff92cbdf)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a book",
            tint = Color.White
        )
    }

}


@Composable
fun BookRating(score: Double = 4.5) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        elevation = 6.dp,
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "",
                modifier = Modifier.padding(3.dp)
            )
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)
        }
    }

}


@Composable
fun ListCard(
    book: MBook,
    onPresessDeatils: (String) -> Unit = {}  //this is not event this will be function woth word that will bae passed on click event to excute
) {

    val context = LocalContext.current
    val resources = context.resources
    //dynamic rakhni hai width
    val displayMetrics = resources.displayMetrics
    val screenwidth = displayMetrics.widthPixels / displayMetrics.density

    //card ke baad wala
    val spacing = 10.dp

    Card(
        shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPresessDeatils.invoke(book.title.toString()) }) {

        Column(
            modifier = Modifier.width(screenwidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            //card ke insede contents set
            Row(horizontalArrangement = Arrangement.Center) {


                Image(
                    painter =
                    //url se image
                    rememberImagePainter(data = book.photoUrl.toString()),
                    contentDescription = "",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )
                //image and pill ke beechme space
                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    BookRating(score = book.rating!!)

                }
            }
            //new row
            Text(
                text = book.title.toString(),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.authors.toString(),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption
            )

            //reading state here
            val isReading = remember {
                mutableStateOf(false)
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                isReading.value = book.startedReading != null
                RoundedButton(label = if (isReading.value) "Reading" else "InList", radius = 70)
            }
        }

    }

}


//ratings bar
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon( //5 peredef widget repart
                painter = painterResource(id = R.drawable.baseline_star_border_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)

                    //dwable pr action observe
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}


//card ke right mw rounded marking
@Composable
fun RoundedButton(label: String = "Reading", radius: Int = 29, onPress: () -> Unit = {}) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(

                // bottomEndPercent = radius,
                // topStartPercent = radius,
                bottomStartPercent = radius
            )
        ),
        color = Color(0XFF92CBDF)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                //comtarints the contents between min and max space inside widget in which it is added
                .heightIn(40.dp)
                .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))

        }
    }
}
