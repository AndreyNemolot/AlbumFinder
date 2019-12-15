package com.example.itunesalbum.fragments.albumList

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunesalbum.model.AlphabetComparatorAscending
import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.album.AlbumResult
import com.example.itunesalbum.network.Controller
import com.example.itunesalbum.repository.MusicRepository
import com.example.itunesalbum.repository.MusicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext


class AlbumListViewModelImpl : ViewModel(), AlbumListViewModel, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO
    override var searchQuery = ""
    private val repository: MusicRepository
    private val liveDataAlbum = MutableLiveData<List<AlbumResult>>()
    private var albumList: MutableList<AlbumResult> = mutableListOf()
    private val liveDataAlbumAutocomplete = MutableLiveData<List<AlbumResult>>()
    private var autocompleteList : MutableList<AlbumResult> = mutableListOf()
    val isResponseEmpty = ObservableBoolean(false)
    val isNetworkProblem = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val doShowSearchHint = ObservableBoolean(true)

    init {
        repository = MusicRepositoryImpl(Controller())
    }

    override fun loadAlbumsByName(query: String, numberOfItems: Int) {
        isLoading.set(true)
        clearMessages()
        launch {
            try {
                val albumResponse = repository.getAlbums(query, numberOfItems)
                handleSuccessResponse(albumResponse)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                handleErrorResponse()
            } finally {
                isLoading.set(false)
                liveDataAlbum.postValue(albumList)
            }

        }
    }

    private fun handleSuccessResponse(albumResponse: AlbumResponse) {
        if (albumResponse.resultCount == 0) {
            doShowSearchHint.set(false)
            isNetworkProblem.set(false)
            isResponseEmpty.set(true)
        } else {
            albumList = albumResponse.results
            clearMessages()
            Collections.sort(albumList, AlphabetComparatorAscending())
        }
    }

    private fun handleErrorResponse() {
        doShowSearchHint.set(false)
        isNetworkProblem.set(true)
        isResponseEmpty.set(false)
    }

    //Clear notification messages
    private fun clearMessages() {
        doShowSearchHint.set(false)
        isNetworkProblem.set(false)
        isResponseEmpty.set(false)
    }

    override fun loadAlbumsForAutocompleteByName(query: String, numberOfItems: Int) {
        launch {
            try {
                val albumResponse = repository.getAlbums(query, numberOfItems)
                handleAutoSuccessResponse(albumResponse)
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            } finally {
                liveDataAlbumAutocomplete.postValue(autocompleteList)
            }
        }
    }

    private fun handleAutoSuccessResponse(albumResponse: AlbumResponse) {
        if (albumResponse.resultCount != 0) {
            autocompleteList = albumResponse.results
            Collections.sort(autocompleteList, AlphabetComparatorAscending())
            liveDataAlbumAutocomplete.postValue(autocompleteList)
        }
    }

    override fun subscribeOnAlbumsList(): LiveData<List<AlbumResult>> {
        return liveDataAlbum
    }

    override fun subscribeOnAutocompleteAlbumsList(): LiveData<List<AlbumResult>> {
        return liveDataAlbumAutocomplete
    }

}


