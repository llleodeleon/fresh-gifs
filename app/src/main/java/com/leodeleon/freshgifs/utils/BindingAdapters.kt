package com.leodeleon.freshgifs.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.Placeholder
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.leodeleon.domain.entities.Image
import com.leodeleon.freshgifs.R


object BindingAdapters {

    @BindingAdapter(value = ["gifImage","placeholderColor"], requireAll = false)
    @JvmStatic
    fun loadGif(view: ImageView, image: Image, @ColorInt placeholderColor: Int? = null){
        val options = RequestOptions().apply {
            diskCacheStrategy(DiskCacheStrategy.NONE)
            placeholderColor?.let {
                val bitmap = getColorPlaceholder(it, image.width.toInt(), image.height.toInt())
                placeholder(bitmap)
            }

        }
        Glide.with(view)
            .asGif()
            .load(image.url)
            .apply(options)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)

    }

    @BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
    @JvmStatic
    fun loadImage(view: ImageView, imageUrl: String, placeholder: Drawable? = null){

        val options = RequestOptions().apply {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            placeholder(placeholder)
        }

        Glide.with(view)
            .load(imageUrl)
            .apply(options)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }

    @BindingAdapter(value = ["selected"])
    @JvmStatic
    fun setSelected(view: View, selected: Boolean){
        view.isSelected = selected
    }

    @BindingAdapter(value = ["isVisible"])
    @JvmStatic
    fun setVisible(view: View, isVisible: Boolean){
        view.isVisible = isVisible
    }

}