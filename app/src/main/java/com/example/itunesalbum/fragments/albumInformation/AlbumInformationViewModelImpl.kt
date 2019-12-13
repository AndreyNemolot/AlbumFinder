package com.example.itunesalbum.fragments.albumInformation

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunesalbum.model.album.AlbumResult
import com.example.itunesalbum.model.song.SongListHeader
import com.example.itunesalbum.model.song.SongResponse
import com.example.itunesalbum.model.song.SongResult
import com.example.itunesalbum.network.Controller
import com.example.itunesalbum.repository.MusicRepository
import com.example.itunesalbum.repository.MusicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AlbumInformationViewModelImpl(val album: AlbumResult) : ViewModel(),
    AlbumInformationViewModel, CoroutineScope {

    override val headerItem: SongListHeader = adaptHeader()
    override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val repository: MusicRepository
    private val songListLiveData = MutableLiveData<List<SongResult>>()
    private var songList = LinkedList<SongResult>()
    val isNetworkProblem = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)

    init {
        repository = MusicRepositoryImpl(Controller())
        loadSongsByAlbumId(album.collectionId)
    }


    override fun loadSongsByAlbumId(albumId: Int) {
        isLoading.set(true)
        launch {
            val songResponse = repository.getSongsById(albumId)
            handleResponse(songResponse)

            isLoading.set(false)
        }
    }

    private fun handleResponse(songResponse: SongResponse) {
        val (resultCount, exception) = songResponse
        when {
            (resultCount == 0 && exception != "") -> isNetworkProblem.set(true)
            else -> {
                songList = songResponse.results
                //remove first element because first element is album kind item
                songList.removeAt(0)
                songListLiveData.postValue(songList)
            }
        }
    }

    override fun subscribeOnSongsList(): LiveData<List<SongResult>> {
        return songListLiveData
    }

    private fun adaptHeader(): SongListHeader {
        return SongListHeader().apply {
            collectionName = album.collectionName
            releaseDate = album.releaseDate
            trackCount = album.trackCount
            artworkUrl100 = album.artworkUrl100
            artistName = album.artistName
            collectionViewUrl = album.collectionViewUrl
            copyright = album.copyright
        }
    }
}
