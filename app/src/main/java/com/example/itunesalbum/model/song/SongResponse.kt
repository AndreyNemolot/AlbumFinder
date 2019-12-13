package com.example.itunesalbum.model.song

import java.util.*

data class SongResponse(
    val resultCount: Int = 0,
    val exception: String = "",
    val results: LinkedList<SongResult> = LinkedList()
)