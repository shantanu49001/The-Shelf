package com.devst.reader.Screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devst.reader.data.DataOrException
import com.devst.reader.model.MBook
import com.devst.reader.repo.BookRepository
import com.devst.reader.repo.FireRepositiry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepositiry) :
    ViewModel() {

    //this is a mutable state as it gets change and needs to redarwa
    val data: MutableState<DataOrException<List<MBook>, Boolean, java.lang.Exception>> =
        mutableStateOf(
            DataOrException(listOf(), true, Exception(""))
        )

    init {
        getAllBooksFromDatabase()
    }

    fun getAllBooksFromDatabase() {

        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooksFromDatabase()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false

        }

        Log.d("GET", "getAllBooksFromDatabase:${data.value.data?.toString()}")
    }

}