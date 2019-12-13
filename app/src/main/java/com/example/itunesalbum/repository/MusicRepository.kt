package com.example.itunesalbum.repository

import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.song.SongResponse

interface MusicRepository {
    suspend fun getAlbums(query: String, numberOfElements: Int): AlbumResponse
    suspend fun getSongsById(id: Int ): SongResponse


}