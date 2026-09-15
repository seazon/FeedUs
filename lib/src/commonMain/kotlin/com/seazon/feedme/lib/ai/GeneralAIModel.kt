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
data class GeneralAIStreamRequest(
    val model: String,
    val messages: List<Message>,
    @SerialName("enable_thinking")
    val enableThinking: Boolean,
    val stream: Boolean,
    @SerialName("max_tokens")
    val maxTokens: Int = 2048,
)

@Serializable
data class Message(
    val role: String? = null, // user / assistant / system, may be absent in a stream delta
    val content: String? = null
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
    val delta: Message? = null, // incremental content used in streaming responses
    val finishReason: String? = null // stop / length
)

@Serializable
data class ErrorInfo(
    val code: String? = null,
    val message: String? = null
)
