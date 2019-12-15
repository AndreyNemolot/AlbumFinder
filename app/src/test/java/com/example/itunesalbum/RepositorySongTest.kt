package com.example.itunesalbum

import com.example.itunesalbum.model.song.SongResponse
import com.example.itunesalbum.repository.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.coroutines.CoroutineContext

class RepositorySongTest : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val repository: MusicRepository = mock(MusicRepository::class.java)
    private val songResponseEmpty = SongResponse(resultCount = 0)
    private val songResponseCorrect = SongResponse(resultCount = 1)

    @Before
    fun setUp() {
        songSetup()
    }

    private fun songSetup() {
        runBlocking {
            launch {
                given(repository.getSongsById(-1)).willAnswer {
                    throw IOException()
                }
                Mockito.`when`(repository.getSongsById(0))
                    .thenReturn(songResponseEmpty)
                Mockito.`when`(repository.getSongsById(1))
                    .thenReturn(songResponseCorrect)
            }.join()
        }
    }

    @Test(expected = IOException::class)
    fun songResponseException() {
        runBlocking {
            launch {
                repository.getSongsById(-1)
                Assert.fail("Should have thrown io exception")
            }.join()
        }
    }

    @Test
    fun songResponseEmpty() {
        var rep = SongResponse()
        runBlocking {
            launch {
                rep = repository.getSongsById(0)
            }.join()
            assert(rep.resultCount == 0)
        }
    }

    @Test
    fun songResponseCorrect() {
        var rep = SongResponse()
        runBlocking {
            launch {
                rep = repository.getSongsById(1)
            }.join()
            assert(rep.resultCount == 1)
        }
    }

}