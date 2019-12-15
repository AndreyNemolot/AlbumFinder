package com.example.itunesalbum.model.album


data class AlbumResponse(
    val resultCount: Int = 0,
    val results: MutableList<AlbumResult> = mutableListOf()
)