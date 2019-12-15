package com.example.itunesalbum.model

import com.example.itunesalbum.model.album.AlbumResult
import com.example.itunesalbum.model.song.SongListHeader

class SongListHeaderAdapter(val album: AlbumResult) {
    fun albumResultToSongListHeader(): SongListHeader {
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