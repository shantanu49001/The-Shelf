package com.devst.reader.Screens.details

import androidx.lifecycle.ViewModel
import com.devst.reader.model.Item
import com.devst.reader.repo.BookRepository
import com.google.rpc.context.AttributeContext.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DeatilsViewModel @Inject constructor(private  val repository: BookRepository):ViewModel() {


    suspend fun getBookInfo(bookId:String):com.devst.reader.data.Resource<Item>{


        return  repository.getBookInfo(bookId)
    }

}