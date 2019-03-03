package com.leodeleon.data.entities

import com.leodeleon.domain.entities.Giphy
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GiphyEntity(
    val id: String,
    val images: ImagesEntity
) {
    constructor(gif: Giphy): this (gif.id, ImagesEntity(gif.images) )

    fun unwrap() = Giphy(
        id, "", images.unwrap(), null
    )
}