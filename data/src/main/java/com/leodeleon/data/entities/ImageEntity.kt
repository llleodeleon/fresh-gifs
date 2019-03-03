package com.leodeleon.data.entities

import com.leodeleon.domain.entities.Image


data class ImageEntity(val url: String, val width: String, val height: String){
    constructor(image: Image): this(image.url, image.width, image.height)
    fun unwrap() = Image(url, width, height)
}