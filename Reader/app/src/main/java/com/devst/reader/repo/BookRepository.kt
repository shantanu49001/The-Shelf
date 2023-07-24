package com.devst.reader.repo

import android.content.ClipData
import android.content.ClipData.Item
import android.util.Log
import com.devst.reader.Network.BooksApi
import com.devst.reader.data.DataOrException
import com.devst.reader.data.Resource
import com.devst.reader.model.Book
import okhttp3.Response
import retrofit2.http.Query
import javax.inject.Inject

//error will be present only in this file


class BookRepository @Inject constructor(private val api: BooksApi) {


    suspend fun getBooks(searchQuery: String): Resource<List<com.devst.reader.model.Item>> {
        return try {
            Resource.Loading(data = "..Loading")
            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Sucess(data = itemList)

        } catch (e: java.lang.Exception) {
            Log.d("Reepo", e.message.toString())

            Resource.Error(message = e.message.toString())
        }
    }


    suspend fun getBookInfo(bookId: String): Resource<com.devst.reader.model.Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)  //last line is return type
            //  Log.d("Reepo",)
        } catch (e: java.lang.Exception) {
            return Resource.Error(message = "Error ")
        }
        Resource.Loading(data = false)
        return Resource.Sucess(data = response)

    }

}