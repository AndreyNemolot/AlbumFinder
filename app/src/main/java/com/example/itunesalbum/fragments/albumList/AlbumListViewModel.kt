package com.example.itunesalbum.fragments.albumList

import androidx.lifecycle.LiveData
import com.example.itunesalbum.model.album.AlbumResult

interface AlbumListViewModel {
    var searchQuery: String
    fun loadAlbumsByName(query: String, numberOfItems: Int = 200)
    fun loadAlbumsForAutocompleteByName(query: String, numberOfItems: Int = 10)
    fun subscribeOnAlbumsList(): LiveData<List<AlbumResult>>
    fun subscribeOnAutocompleteAlbumsList(): LiveData<List<AlbumResult>>

}