package com.leodeleon.domain

data class Giphy(
    val id: String,
    val source_tld: String,
    val images: Images,
    val user: User?
)