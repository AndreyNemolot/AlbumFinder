package  com.example.itunesalbum.network

import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.song.SongResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RssService {
    @GET("search")
    fun getAlbums(
        @Query("term") term: String,
        @Query("entity") entity: String,
        @Query("attribute") attribute: String,
        @Query("limit") limit: Int
    ): Call<AlbumResponse>

    @GET("lookup")
    fun getSongs(
        @Query("id") id: Int,
        @Query("entity") entity: String
    ): Call<SongResponse>
}