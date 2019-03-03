package com.leodeleon.freshgifs

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()


fun TabLayout.listen(onSelected: (TabLayout.Tab) -> Unit = {}){
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) { }

        override fun onTabUnselected(tab: TabLayout.Tab) { }

        override fun onTabSelected(tab: TabLayout.Tab) {
            onSelected(tab)
        }
    })
}