package com.leodeleon.domain.entities

data class ApiResponse(
    val data: List<Giphy>,
    val pagination: Pagination
)