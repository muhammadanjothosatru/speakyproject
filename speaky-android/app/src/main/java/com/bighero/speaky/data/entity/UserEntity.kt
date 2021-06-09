package com.bighero.speaky.data.entity

data class UserEntity(
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val imgPhoto: String? = null,
    val level: String? = null,
    val status: Boolean? = false,
    val score: Int = 0
)
