package  com.example.itunesalbum.network


import com.example.itunesalbum.model.album.AlbumResponse
import com.example.itunesalbum.model.song.SongResponse
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Controller {
    private val baseUrl = "https://itunes.apple.com/"
    private var client: Retrofit
    private val albumEntity="album"
    private val songEntity="song"
    private val albumAttribute="albumTerm"


    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        client = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun getAlbumsByName(query: String, numberOfElements: Int): Call<AlbumResponse> {
        val service = client.create(RssService::class.java)
        return service.getAlbums(query, albumEntity, albumAttribute, numberOfElements)
    }

    fun getSongsByAlbumId(id: Int): Call<SongResponse> {
        val service = client.create(RssService::class.java)
        return service.getSongs(id, songEntity)
    }

}