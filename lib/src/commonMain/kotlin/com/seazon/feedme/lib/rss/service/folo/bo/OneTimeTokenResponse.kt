package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class OneTimeTokenResponse(
    val user: User
) {
    @Serializable
    data class User(
        val name: String,
        val email: String,
        val emailVerified: Boolean,
        val image: String,
        val createdAt: String,
        val updatedAt: String,
//    val twoFactorEnabled:String,
//    val handle:String,
        val id: String,
    )
}


