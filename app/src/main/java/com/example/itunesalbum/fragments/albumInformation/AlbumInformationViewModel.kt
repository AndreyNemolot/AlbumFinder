package com.example.itunesalbum.fragments.albumInformation

import androidx.lifecycle.LiveData
import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.song.SongListHeader
import com.example.itunesalbum.model.song.SongResult

interface AlbumInformationViewModel {
    fun loadSongsByAlbumId(albumId: Int)
    fun subscribeOnSongsList(): LiveData<List<SongResult>>
    val headerItem: SongListHeader

}