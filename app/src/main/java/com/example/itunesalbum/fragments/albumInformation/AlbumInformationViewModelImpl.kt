package com.example.itunesalbum.fragments.albumInformation

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunesalbum.model.SongListHeaderAdapter
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
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class AlbumInformationViewModelImpl(val album: AlbumResult) : ViewModel(),
    AlbumInformationViewModel, CoroutineScope {

    override val headerItem: SongListHeader = SongListHeaderAdapter(
        album
    )
        .albumResultToSongListHeader()
    override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val repository: MusicRepository
    private val songListLiveData = MutableLiveData<List<SongResult>>()
    private var songList: MutableList<SongResult> = mutableListOf()
    val isNetworkProblem = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)

    //Load information by chosen album after viewModel created
    init {
        repository = MusicRepositoryImpl(Controller())
        loadSongsByAlbumId(album.collectionId)
    }

    override fun loadSongsByAlbumId(albumId: Int) {
        isLoading.set(true)
        launch {
            try {
                val songResponse = repository.getSongsById(albumId)
                handleSuccessResponse(songResponse)
            } catch (ioException: Exception) {
                isNetworkProblem.set(true)
            } finally {
                isLoading.set(false)
            }
        }
    }

    private fun handleSuccessResponse(songResponse: SongResponse) {
        if (songResponse.resultCount != 0) {
            songList = songResponse.results
            //remove first element because first element is album kind item
            if (songList.size > 0) songList.removeAt(0)
            songListLiveData.postValue(songList)
        }
    }

    override fun subscribeOnSongList(): LiveData<List<SongResult>> {
        return songListLiveData
    }

}
