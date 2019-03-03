package com.leodeleon.domain

data class Giphy(
    val id: String,
    val url: String,
    val source_tld: String,
    val user: User?
)