package com.leodeleon.domain

data class Pagination(
    val total_count: Int,
    val count: Int,
    val offset: Int
)