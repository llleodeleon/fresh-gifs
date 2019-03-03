package com.leodeleon.data.entities

import com.leodeleon.domain.entities.Images

data class ImagesEntity(val fixed_width: ImageEntity, val fixed_width_still: ImageEntity){
    constructor(images: Images): this(ImageEntity(images.fixed_width), ImageEntity(images.fixed_width_still))

    fun unwrap() = Images(fixed_width.unwrap(), fixed_width_still.unwrap())
}