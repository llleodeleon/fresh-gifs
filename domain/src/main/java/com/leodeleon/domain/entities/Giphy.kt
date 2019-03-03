package com.leodeleon.domain.entities

data class Giphy(
    val id: String,
    val source_tld: String,
    val images: Images,
    val user: User?
)