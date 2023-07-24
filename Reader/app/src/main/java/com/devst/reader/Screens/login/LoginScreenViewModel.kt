package com.devst.reader.Screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devst.reader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //coz it has to be main afe as ye kaam krke ui update karega
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    //call back
    fun signInUserWithEmailPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->  //info object return
                        if (task.isSuccessful) {
                            home()  //event
                        } else {
                            println("YAAY")
                            println(task.result.toString())
                        }
                    }
            } catch (e: java.lang.Exception) {
                println(e)
            }
        }

    fun createUserWithEmailPassword(
        email: String,
        password: String,
        home: () -> Unit  //event hone pr idhar ye function run run dena
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val displayName = it.result?.user?.email?.split('@')
                            ?.get(0)  //one lement befor @ on after @
                        //make this user's collection s soon as we regiter new user

                        createUser(displayName)
                        home()
                    } else {
                        println(it.result.toString())
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId =
            auth.currentUser?.uid  //firebase user that is currently logged in from thi device -->direclty
       //firestore me upadte bhi kr sakte hai user data ko
        val user = MUser(
            userId=userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is Greate",
            profession = "Android Dvfeloper",
            id = null

        ).toMap()    //data collection me chnage

        //add this user's data collection place  if collection not present ; it will be made
        FirebaseFirestore.getInstance().collection("users")
            .add(user)  //indiv data plce for the user


    }

}




