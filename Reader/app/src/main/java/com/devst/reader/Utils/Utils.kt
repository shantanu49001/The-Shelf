package com.devst.reader.Utils

import android.icu.text.DateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp

@RequiresApi(Build.VERSION_CODES.N)
fun formatDate(timestamp: Timestamp): String {
    val date = DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString().split(",")
    return date[0]
}