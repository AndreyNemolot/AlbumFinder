package com.example.itunesalbum.repository

import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.song.SongResponse
import com.example.itunesalbum.network.Controller
import java.lang.Exception

class MusicRepositoryImpl(private val controller: Controller) : MusicRepository {

    override suspend fun getAlbums(query: String, numberOfElements: Int): AlbumResponse {
        return try {
            controller.getAlbumsByName(query, numberOfElements).await()
        } catch (e: Exception) {
            AlbumResponse(exception = e.toString())
        }
    }

    override suspend fun getSongsById(id: Int): SongResponse {
        return try {
            controller.getSongsByAlbumId(id).await()
        } catch (e: Exception) {
            SongResponse(exception = e.toString())
        }
    }
}