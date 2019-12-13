package com.example.itunesalbum.model.song

import android.view.View
import com.example.itunesalbum.R
import com.example.itunesalbum.databinding.HeaderSongListItemBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import java.text.SimpleDateFormat


data class SongListHeader(
    var collectionName: String = "",
    var releaseDate: String = "",
    var trackCount: Int = 0,
    var artworkUrl100: String = "",
    var artistName: String = "",
    var collectionViewUrl: String = "",
    var copyright: String = ""

) : AbstractItem<SongListHeader.ViewHolder>() {

    fun setDataFormat(date:String):String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(parser.parse(date))
    }

    override fun toString(): String {
        return collectionName
    }

    override val type: Int
        get() = R.id.header_song_list

    override val layoutRes: Int
        get() = R.layout.header_song_list_item

    override var isEnabled: Boolean
        get() = true
        set(value) {}

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<SongListHeader>(view) {
        private val binding = HeaderSongListItemBinding.bind(itemView)

        override fun unbindView(item: SongListHeader) {
            binding.header = null
        }

        override fun bindView(item: SongListHeader, payloads: MutableList<Any>) {
            binding.header = item
        }
    }
}