package com.devst.reader.model

data class Book(
    val items: List<Item>,  //main
    val kind: String,
    val totalItems: Int
)