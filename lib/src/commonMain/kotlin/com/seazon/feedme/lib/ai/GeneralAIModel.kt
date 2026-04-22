package com.seazon.feedme.lib.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneralAIRequest(
    val model: String,
    val messages: List<Message>,
    @SerialName("enable_thinking")
    val enableThinking: Boolean,
)

@Serializable
data class Message(
    val role: String, // user /assistant /system
    val content: String
)

@Serializable
data class GeneralAIResponse(
    val id: String? = null,
    val choices: List<Choice>? = null,
    val error: ErrorInfo? = null
)

@Serializable
data class Choice(
    val message: Message? = null,
    val finishReason: String? = null // stop / length
)

@Serializable
data class ErrorInfo(
    val code: String? = null,
    val message: String? = null
)
