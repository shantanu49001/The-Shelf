package com.devst.reader.repo

import com.devst.reader.data.DataOrException
import com.devst.reader.model.MBook
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//query of firebse forestore
class FireRepositiry @Inject constructor(
    private val queryBook: Query
) {
    //firebase firestore se query pick   app module me wahan tak already reach
    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, java.lang.Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, java.lang.Exception>()


        try {

            dataOrException.loading = true
            //waits in bg thread
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!

            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        } catch (exception: java.lang.Exception) {
            dataOrException.e = exception
        }

        return dataOrException
    }
}