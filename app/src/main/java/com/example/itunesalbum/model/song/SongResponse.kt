package com.example.itunesalbum.model.song


data class SongResponse(
    val resultCount: Int = 0,
    val results: MutableList<SongResult> = mutableListOf()
)