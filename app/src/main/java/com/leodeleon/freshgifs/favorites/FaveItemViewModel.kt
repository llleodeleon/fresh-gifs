package com.leodeleon.freshgifs.favorites

import android.view.View
import androidx.annotation.ColorInt
import androidx.databinding.ObservableBoolean
import com.leodeleon.domain.entities.Giphy

class FaveItemViewModel(val item: Giphy, @ColorInt val placeholder: Int, private val onDelete: (Giphy) -> Unit = {}) {

    val showDelete = ObservableBoolean()
    val image = item.images.fixed_width

    val onLongPress = View.OnLongClickListener {
        it.post { it.alpha = 0.2f }
        showDelete.set(!showDelete.get())
        true
    }

    val onClickDelete = View.OnClickListener {
        onDelete(item)
    }

    val onClick = View.OnClickListener {
        if(showDelete.get()){
            it.post { it.alpha = 1.0f }
            showDelete.set(false)
        }
    }

}