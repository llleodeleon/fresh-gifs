package com.leodeleon.freshgifs.favorites

import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.ObservableBoolean
import com.leodeleon.domain.entities.Giphy

class FaveItemViewModel(val item: Giphy, val placeholder: Drawable, private val onDelete: (Giphy) -> Unit = {}) {

    val showDelete = ObservableBoolean()
    val image = item.images.fixed_width
    val imageUrl = item.images.fixed_width_still.url

    val onLongPress = View.OnLongClickListener {
        showDelete.set(!showDelete.get())
        true
    }

    val onClickDelete = View.OnClickListener {
        onDelete(item)
    }

}