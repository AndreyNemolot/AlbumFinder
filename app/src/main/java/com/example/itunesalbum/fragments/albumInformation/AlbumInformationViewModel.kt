package com.example.itunesalbum.fragments.albumInformation

import androidx.lifecycle.LiveData
import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.song.SongListHeader
import com.example.itunesalbum.model.song.SongResult

interface AlbumInformationViewModel {
    fun loadSongsByAlbumId(albumId: Int)
    fun subscribeOnSongList(): LiveData<List<SongResult>>
    //First item in songs list which show information about album
    val headerItem: SongListHeader

}