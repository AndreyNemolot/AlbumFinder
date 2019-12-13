package com.example.itunesalbum.fragments.albumList

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.album.AlbumResult
import com.example.itunesalbum.network.Controller
import com.example.itunesalbum.repository.MusicRepository
import com.example.itunesalbum.repository.MusicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class AlbumListViewModelImpl : ViewModel(), AlbumListViewModel, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO
    override var searchQuery = ""
    private val repository: MusicRepository
    private val liveDataAlbums = MutableLiveData<List<AlbumResult>>()
    private val liveDataAlbumsAutocomplete = MutableLiveData<List<AlbumResult>>()
    private var albumList = ArrayList<AlbumResult>()
    private var autocompleteList = ArrayList<AlbumResult>()
    val isResponseEmpty = ObservableBoolean(false)
    val isNetworkProblem = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val doShowHint = ObservableBoolean(false)

    init {
        repository = MusicRepositoryImpl(Controller())
    }

    override fun loadAlbumsByName(query: String, numberOfItems: Int) {
        isLoading.set(true)
        removeMessages()
        launch {
            val albumResponse = repository.getAlbums(query, numberOfItems)
            handleResponse(albumResponse)
            isLoading.set(false)
        }
    }

    private fun handleResponse(albumResponse: AlbumResponse) {
        albumList = albumResponse.results
        val (resultCount, exception) = albumResponse
        when {
            (resultCount == 0 && exception == "") -> {
                albumList=ArrayList()
                isNetworkProblem.set(false)
                isResponseEmpty.set(true)
            }
            (resultCount == 0 && exception != "") -> {
                albumList=ArrayList()
                isNetworkProblem.set(true)
                isResponseEmpty.set(false)
            }
            else -> {
                removeMessages()
                Collections.sort(albumList, AlphabetComparatorAscending())
            }
        }
        liveDataAlbums.postValue(albumList)


    }

    private fun removeMessages(){
        isNetworkProblem.set(false)
        isResponseEmpty.set(false)
    }

    override fun loadAlbumsForAutocompleteByName(query: String, numberOfItems: Int) {
        launch {
            albumList = repository.getAlbums(query, numberOfItems).results
            Collections.sort(albumList, AlphabetComparatorAscending())
            liveDataAlbumsAutocomplete.postValue(albumList)
            autocompleteList.addAll(albumList)
        }
    }

    override fun subscribeOnAlbumsList(): LiveData<List<AlbumResult>> {
        return liveDataAlbums
    }

    override fun subscribeOnAutocompleteAlbumsList(): LiveData<List<AlbumResult>> {
        return liveDataAlbumsAutocomplete
    }

    private inner class AlphabetComparatorAscending : Comparator<AlbumResult>, Serializable {
        override fun compare(lhs: AlbumResult, rhs: AlbumResult): Int {
            return lhs.collectionName.compareTo(rhs.collectionName)
        }
    }
}


