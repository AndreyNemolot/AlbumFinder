package com.example.itunesalbum.model.song

import android.view.View
import com.example.itunesalbum.R
import com.example.itunesalbum.databinding.SongListItemBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

//Model for song item result which contains view holder for FastAdapter

data class SongResult(
    val artistId: Int=0,
    val artistName: String= "",
    val artworkUrl30: String="",
    val artworkUrl60: String="",
    val collectionId: Int=0,
    val collectionName: String="",
    val trackName: String="",
    val trackNumber: Int= 0,
    val trackViewUrl: String,
    val wrapperType: String=""
): AbstractItem<SongResult.ViewHolder>(){

    override val layoutRes: Int
        get() = R.layout.song_list_item

    override val type: Int
        get() = trackNumber

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<SongResult>(view) {
        private val binding = SongListItemBinding.bind(itemView)

        override fun unbindView(item: SongResult) {
            binding.songItem = null
        }

        override fun bindView(item: SongResult, payloads: MutableList<Any>) {
            binding.songItem = item
        }
    }
}