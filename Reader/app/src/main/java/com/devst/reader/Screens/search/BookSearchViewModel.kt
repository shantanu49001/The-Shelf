package com.devst.reader.Screens.search

import android.content.ClipData
import android.content.ClipData.Item
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devst.reader.data.DataOrException
import com.devst.reader.data.Resource
import com.devst.reader.repo.BookRepository
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {


    var list: List<com.devst.reader.model.Item> by mutableStateOf(listOf())

    var isLoading:Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

     fun loadBooks() {
    //    TO DO("Not yet implemented")

       searchBooks("android")
     }

     fun searchBooks(query: String) {
     viewModelScope.launch {

         isLoading=true
         if (query.isEmpty()){
             return@launch
         }
         try {
             when(val response=repository.getBooks(query)){
                 is Resource.Sucess-> {  //if we were sucef
                     list=response.data!!

                     if (list.isNotEmpty()) isLoading=false

                 }
                 is Resource.Error->{
                     Log.e("Network","falied ")
                 }
                 else->{
                     isLoading=false
                 }


             }

         }catch (exception:java.lang.Exception){
           Log.d("Network","Error exp")
         }
     }
    }
}