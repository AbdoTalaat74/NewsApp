package com.example.newsapp.utils

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("photoUrl")
fun bindImage(imageView: ImageView, url: String?) {
    url?.let {
        val photoUri = url.toUri().buildUpon().build()
        Picasso.with(imageView.context).load(photoUri).into(imageView)
    }
}