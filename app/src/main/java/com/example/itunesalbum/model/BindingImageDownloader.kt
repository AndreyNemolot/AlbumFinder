package com.example.itunesalbum.model

import android.os.Build
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.itunesalbum.R

//Adapter for download image from data binding

class BindingImageDownloader {
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
}