package com.seazon.feedme.lib.rss.service.folo.bo

import kotlinx.serialization.Serializable

@Serializable
data class OneTimeTokenResponse(
    val user: User? = null,
) {
    @Serializable
    data class User(
        val name: String? = null,
        val email: String? = null,
        val emailVerified: Boolean? = null,
        val image: String? = null,
        val createdAt: String? = null,
        val updatedAt: String? = null,
//    val twoFactorEnabled:String? = null,
//    val handle:String? = null,
        val id: String? = null,
    )
}


