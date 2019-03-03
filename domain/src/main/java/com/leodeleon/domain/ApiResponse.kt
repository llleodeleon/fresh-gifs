package com.leodeleon.domain

data class ApiResponse(
    val data: List<Giphy>,
    val pagination: Pagination
)