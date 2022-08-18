package com.example.ednanotebook.db

data class Note(
    var title: String? = null,
    var desc: String? = null,
    var time: String? = null,
    var uri: String = "empty"
)
