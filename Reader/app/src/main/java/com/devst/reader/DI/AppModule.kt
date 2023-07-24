package com.devst.reader.DI

import com.devst.reader.Network.BooksApi
import com.devst.reader.Utils.Constants
import com.devst.reader.Utils.Constants.BASE_URL
import com.devst.reader.repo.BookRepository
import com.devst.reader.repo.FireRepositiry
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


//all the instances that need to get initlised by hilt

@Module
@InstallIn(SingletonComponent::class)   //all thesw inances are sngleton
object AppModule {

    @Singleton
    @Provides
    fun provideFireBookReposity() =FireRepositiry(queryBook = FirebaseFirestore.getInstance().collection("books"))




    //api must be intited by hlti in application and instance made here
    @Singleton
    @Provides
    fun provideBookApi(): BooksApi {  //function wrpas our created api with retrofit

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)  //ierface jidhar function hai

    }


    //repo dependecy
    @Singleton
    @Provides
    fun provideBookReposoory(api: BooksApi)=BookRepository(api)







}