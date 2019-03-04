package com.leodeleon.freshgifs.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.leodeleon.freshgifs.R
import com.leodeleon.freshgifs.app.App

fun Any.logd(msg:String, tag: String = this.javaClass.simpleName){
    Log.d(tag,msg)
}

fun getString(@StringRes resId: Int): String {
    return App.instance.getString(resId)
}

fun getColor(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(App.instance, resId)
}

fun getColorPlaceholder(@ColorInt color: Int, width:Int, height: Int): BitmapDrawable{
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        this.color = color
        style = Paint.Style.FILL
    }
    canvas.drawPaint(paint)
    return BitmapDrawable(Resources.getSystem(),bitmap)
}

fun getColorFromPosition(position: Int): Int{
    return when {
        position % 5 == 0 -> getColor(R.color.yellow)
        position % 5 == 1 -> getColor(R.color.pink)
        position % 5 == 2 -> getColor(R.color.green)
        position % 5 == 3 -> getColor(R.color.blue)
        position % 5 == 4 -> getColor(R.color.purple)
        else -> getColor(R.color.yellow)
    }
}

fun Context.toast(@StringRes resId: Int, length: Int = Toast.LENGTH_LONG){
    Toast.makeText(this,resId,length).show()
}

fun TabLayout.listen(onSelected: (TabLayout.Tab) -> Unit = {}){
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) { }

        override fun onTabUnselected(tab: TabLayout.Tab) { }

        override fun onTabSelected(tab: TabLayout.Tab) {
            onSelected(tab)
        }
    })
}