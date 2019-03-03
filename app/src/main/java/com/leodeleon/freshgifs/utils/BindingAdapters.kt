package com.leodeleon.freshgifs.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.leodeleon.domain.entities.Image


object BindingAdapters {

    @BindingAdapter(value = ["gifImage","placeholder"], requireAll = false)
    @JvmStatic
    fun loadGif(view: ImageView, image: Image, placeholder: Drawable? = null){
        val options = RequestOptions().apply {
            diskCacheStrategy(DiskCacheStrategy.NONE)
            placeholder(placeholder)
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