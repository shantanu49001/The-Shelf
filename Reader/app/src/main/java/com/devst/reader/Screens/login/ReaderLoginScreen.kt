package com.devst.reader.Screens.login

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.devst.reader.R
import com.devst.reader.components.PasswordInput
import com.devst.reader.components.ReaderLogo
import com.devst.reader.components.emailInput
import com.devst.reader.navigation.ReaderScreens

@OptIn(ExperimentalComposeUiApi::class)
//user sign in ka kaam view moel ka hai ui ka nhi
@Composable
fun ReaderLoginScreen(
    navHostController: NavHostController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    //reuse same screen as signup screen
    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReaderLogo()
            if (showLoginForm.value) {
                UserForm(loading = false, isCreateAccount = false) { email, passw ->
                    Log.d("FORM", "$email $passw")
                    //login fuctonality in loginvm      //atual callback work  -->pass this work to do in callback
                    viewModel.signInUserWithEmailPassword(email, passw) {
                        navHostController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }

                }
            } else {
                UserForm(loading = false, isCreateAccount = true) { email, passw ->
                    Log.d("FORM", "$email $passw")
                    //homemlabda event sicess one pr run
                    viewModel.createUserWithEmailPassword(email, passw) {
                        navHostController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }

                }
            }

        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val text = if (showLoginForm.value) "SignUp " else "Login"
            Text(text = "New User?")
            Text(
                text = text,
                modifier = Modifier
                    .clickable {
                        showLoginForm.value =
                            !showLoginForm.value  //signuo se login lofin se signup
                    }
                    .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,  //sumit button ko ebealbe krna hai ya disble
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, passw -> }

) {  //kaaf compenets hai isme
    //neeche kaafi components hai lekin state parent form pr hi hoga
    val email = rememberSaveable {  //config changes bhi survive kr leta hai rembersavable
        mutableStateOf("")

    }  //state passed onvalue chage me it return
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisisbilty = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFoucesRequest = FocusRequester.Default
    //keboard show hofde features and selecting tyoe of keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    //keboard appears opop up as sson as kisi me click hoga to form area ko scrollbale banana padega

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    //main form is scrollable
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        //ek email ki aur ek password ki text fied repaeting hai

        //show this text if signuo else hige

        if (isCreateAccount) Text(
            text = stringResource(id = R.string.Createaccout),
            modifier = Modifier.padding(4.dp)
        )
        else Text(text = "")

        //state iske pas hi hai ochange event me ye yahi return krta hai
        emailInput(emailState = email, enabled = !loading, onAction = KeyboardActions {
            passwordFoucesRequest.requestFocus()  //change the ficus onve done
        })


        //relevant spacing column apne aap dega margin wargin aaded nhi hai widgets me
        //gtes the focus jidar ye param tha
        PasswordInput(modifier = Modifier.focusRequester(passwordFoucesRequest),
            passwordState = password,
            labelId = "",
            enabled = !loading,
            passwordVisisbilty = passwordVisisbilty,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            })


        //ato avoid copying params again and agai ek baar error ke saath idhar hi code
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login", //dynamic text of button
            loading = loading,
            validInputs = valid
        ) {  //lambda para
            onDone(email.value.trim(), password.value.trim())
            //now ilde the keyboard
            keyboardController?.hide()
        }

    }


}

//  !=  and = ! me difference hai

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        enabled = !loading && validInputs,  //jab ye dono true tabhi lickable
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }

}



