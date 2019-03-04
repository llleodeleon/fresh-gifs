package com.leodeleon.freshgifs.favorites

import android.view.View
import androidx.annotation.ColorInt
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableFloat
import com.leodeleon.domain.entities.Giphy

class FaveItemViewModel(val item: Giphy, @ColorInt val placeholder: Int, private val onDelete: (Giphy) -> Unit = {}) {

    val showDelete = ObservableBoolean()
    val image = item.images.fixed_width
    val alpha = ObservableFloat(1.0f)

    val onLongPress = View.OnLongClickListener {
        if(!showDelete.get()){
            alpha.set(0.2f)
            showDelete.set(true)
        }
        true
    }

    val onClickDelete = View.OnClickListener {
        onDelete(item)
    }

    val onClick = View.OnClickListener {
        if(showDelete.get()){
            alpha.set(1.0f)
            showDelete.set(false)
        }
    }

}