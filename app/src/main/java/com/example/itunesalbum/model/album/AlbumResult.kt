package com.example.itunesalbum.model.album

import android.os.Build
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.itunesalbum.R
import com.example.itunesalbum.databinding.AlbumListItemBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.parcel.Parcelize

@Parcelize

data class AlbumResult(
    var amgArtistId: Int = 0,
    var artistId: Int = 0,
    var artistName: String = "",
    var artistViewUrl: String = "",
    var artworkUrl100: String = "",
    var collectionCensoredName: String = "",
    var collectionExplicitness: String = "",
    var collectionId: Int = 0,
    var collectionName: String = "",
    var collectionPrice: Double = 0.0,
    var collectionType: String = "",
    var collectionViewUrl: String = "",
    var currency: String = "",
    var releaseDate: String = "",
    var trackCount: Int = 0,
    var wrapperType: String = "",
    var copyright: String = ""

) : AbstractItem<AlbumResult.ViewHolder>(), Parcelable {

    companion object {
        private val placeholderId = if (Build.VERSION.SDK_INT >= 24) {
            R.drawable.progress_animation
        } else {
            R.drawable.progress_image
        }

        @JvmStatic
        @BindingAdapter("app:url")
        fun loadImage(view: ImageView, url: String) {
            Glide.with(view.context)
                .load(url)
                .placeholder(placeholderId)
                .error(R.drawable.baseline_image_black)
                .into(view)
        }
    }

    override fun toString(): String {
        return collectionName
    }

    override val type: Int
        get() = R.id.album_list_item

    override val layoutRes: Int
        get() = R.layout.album_list_item

    override var isEnabled: Boolean
        get() = true
        set(value) {}

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
