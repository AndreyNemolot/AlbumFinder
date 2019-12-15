package com.example.itunesalbum.fragments.albumInformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itunesalbum.model.album.AlbumResult

//Factory for creating viewModel class with album parameter

@Suppress("UNCHECKED_CAST")
class AlbumInfoModelFactory(val album: AlbumResult) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlbumInformationViewModelImpl(album) as T
    }
}