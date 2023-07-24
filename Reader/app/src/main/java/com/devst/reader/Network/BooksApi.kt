package com.devst.reader.Network

import com.devst.reader.model.Book
import com.devst.reader.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String):Book

    @GET("volumes/{bookId}")   //search feature implement
    suspend fun getBookInfo(@Path("bookId") bookId:String): Item  //direclty api pr earch
   //rot me se nesed

}