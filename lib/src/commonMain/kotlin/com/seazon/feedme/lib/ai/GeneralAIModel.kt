package com.seazon.feedme.lib.ai

import kotlinx.serialization.Serializable

/**
 * 文生文请求参数DTO（适配主流AI平台的通用格式）
 */
@Serializable
data class Text2TextRequest(
    val model: String,          // 具体模型名称
    val messages: List<Message>,// 对话消息列表（文生文核心）
    val max_tokens: Int = 2000, // 最大生成token数
    val temperature: Double = 0.7 // 生成随机性（0-1，值越高越随机）
)

/**
 * 对话消息体（用户/助手消息）
 */
@Serializable
data class Message(
    val role: String,  // 角色：user（用户）/assistant（助手）/system（系统）
    val content: String // 消息内容
)

/**
 * 通用文生文响应DTO（简化版，适配主流平台返回格式）
 */
@Serializable
data class Text2TextResponse(
    val id: String? = null,
    val choices: List<Choice>? = null,
    val error: ErrorInfo? = null // 错误信息（部分平台返回）
)

@Serializable
data class Choice(
    val message: Message? = null,
    val finish_reason: String? = null // 结束原因：stop（正常）/length（长度超限）
)

@Serializable
data class ErrorInfo(
    val code: String? = null,
    val message: String? = null
)
