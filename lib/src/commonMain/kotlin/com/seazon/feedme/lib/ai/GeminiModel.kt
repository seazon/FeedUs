package com.seazon.feedme.lib.ai

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    val contents: List<Content>? = null,
)

@Serializable
data class Result(
    val candidates: List<Candidates>? = null,
    val error: Error? = null,
) {
    @Serializable
    data class Error(
        val code: Int? = null,
        val message: String? = null,
    )
}

@Serializable
data class Candidates(
    val content: Content? = null,
)

@Serializable
data class Content(
    val parts: List<Part>? = null,
)

@Serializable
data class Part(
    val text: String? = null,
)

@Serializable
data class Translation(
    val dst: String? = null,
)
