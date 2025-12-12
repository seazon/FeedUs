package com.seazon.feedme.lib.summary

import com.seazon.feedme.lib.ai.gemini.AiConfig
import com.seazon.feedme.lib.ai.gemini.AiException
import com.seazon.feedme.lib.ai.gemini.TranslationServiceFactory
import com.seazon.feedme.lib.ai.gemini.AiProvider
import io.ktor.client.network.sockets.SocketTimeoutException

object SummaryUtil {

    /**
     * 执行摘要功能
     * @param query 原文内容
     * @param language 目标语言
     * @param config AI 配置对象 (包含 provider, apiKey, model, baseUrl)
     */
    suspend fun summary(query: String, language: String, config: AiConfig): String? {
        // 获取当前的提供商名称，用于日志 tag，例如 [OPENAI] 或 [GOOGLE_GEMINI]
        val providerTag = config.provider.name

        // 如果用户选择了传统 Google 翻译，它不支持摘要，直接抛出或返回空
        if (config.provider == AiProvider.GOOGLE_TRANSLATE_V2) {
             throw Exception("[$providerTag] summary error: This provider does not support summary features.")
        }

        return try {
            // 1. 通过工厂获取服务实例 (多态)
            val service = TranslationServiceFactory.create(config)

            // 2. 调用通用的 summary 接口
            val result = service.summary(query, language)

            // 3. 返回结果
            result?.dst

        } catch (e: AiException) {
            e.printStackTrace()
            // 捕获 AI 业务异常 (如 Key 错误, Quota 不足)
            throw Exception("[$providerTag] summary error: [${e.code}] ${e.message}")

        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            // 捕获网络超时
            throw Exception("[$providerTag] summary error: socket timeout")

        } catch (e: Exception) {
            e.printStackTrace()
            // 捕获其他未知异常 (解析错误等)
            throw Exception("[$providerTag] summary error: ${e.message ?: "request failed"}")
        }
    }
}
