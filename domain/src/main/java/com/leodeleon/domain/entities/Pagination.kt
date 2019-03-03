package com.leodeleon.domain.entities

data class Pagination(
    val total_count: Int,
    val count: Int,
    val offset: Int
)