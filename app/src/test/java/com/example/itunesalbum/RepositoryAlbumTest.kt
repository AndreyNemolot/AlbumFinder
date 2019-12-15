package com.example.itunesalbum

import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.repository.MusicRepository
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.BDDMockito.given
import java.io.IOException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class RepositoryAlbumTest : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val repository: MusicRepository = mock(MusicRepository::class.java)
    private val albumResponseEmpty = AlbumResponse(resultCount = 0)
    private val albumResponseCorrect = AlbumResponse(resultCount = 10)

    @Before
    fun setUp() {
        albumSetup()
    }

    private fun albumSetup() {
        runBlocking {
            launch {
                given(repository.getAlbums("testException", 0)).willAnswer{
                    throw IOException()
                }
                Mockito.`when`(repository.getAlbums("testEmpty", 0))
                    .thenReturn(albumResponseEmpty)
                Mockito.`when`(repository.getAlbums("testCorrect", 10))
                    .thenReturn(albumResponseCorrect)
            }.join()
        }
    }

    @Test(expected = IOException::class)
    fun albumResponseException() {
        runBlocking {
            launch {
                repository.getAlbums("testException", 0)
                Assert.fail("Should have thrown io exception")
            }.join()
        }
    }

    @Test
    fun albumResponseEmpty() {
        var rep = AlbumResponse()
        runBlocking {
            launch {
                rep = repository.getAlbums("testEmpty", 0)
            }.join()
            assert(rep.resultCount == 0)
        }
    }

    @Test
    fun albumResponseCorrect() {
        var rep = AlbumResponse()
        runBlocking {
            launch {
                rep = repository.getAlbums("testCorrect", 10)
            }.join()
            assert(rep.resultCount == 10)
        }
    }

}