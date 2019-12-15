package com.example.itunesalbum.model.album

import android.os.Parcelable
import android.view.View
import com.example.itunesalbum.R
import com.example.itunesalbum.databinding.AlbumListItemBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.parcel.Parcelize

//Model for album item result which contains view holder for FastAdapter

@Parcelize
data class AlbumResult(
    var artistName: String = "",
    var artworkUrl100: String = "",
    var collectionId: Int = 0,
    var collectionName: String = "",
    var collectionPrice: Double = 0.0,
    var collectionViewUrl: String = "",
    var currency: String = "",
    var releaseDate: String = "",
    var trackCount: Int = 0,
    var copyright: String = ""
) : AbstractItem<AlbumResult.ViewHolder>(), Parcelable {

    override fun toString() = collectionName

    override val type: Int
        get() = collectionId

    override val layoutRes: Int
        get() = R.layout.album_list_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<AlbumResult>(view) {
        private val binding = AlbumListItemBinding.bind(itemView)

        override fun unbindView(item: AlbumResult) {
            binding.albumItem = null
        }

        override fun bindView(item: AlbumResult, payloads: MutableList<Any>) {
            binding.albumItem = item
        }
    }
}
