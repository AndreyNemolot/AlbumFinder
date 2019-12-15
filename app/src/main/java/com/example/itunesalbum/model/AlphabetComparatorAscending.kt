package com.example.itunesalbum.model

import com.example.itunesalbum.model.album.AlbumResult
import java.io.Serializable
import java.util.Comparator

class AlphabetComparatorAscending : Comparator<AlbumResult>, Serializable {
    override fun compare(lhs: AlbumResult, rhs: AlbumResult): Int {
        return lhs.collectionName.compareTo(rhs.collectionName)
    }
}