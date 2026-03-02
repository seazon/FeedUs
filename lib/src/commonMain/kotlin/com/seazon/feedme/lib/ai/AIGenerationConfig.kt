package com.seazon.feedme.lib.ai

enum class AIModel {
    Gemini,
    Volces,
    GLM,
    Claude,
    DeepSeek,
    OpenAI,
    Ernie,
    QWen,
    Dream,
    Spark,
    Custom,
}

data class AIGenerationConfig(
    val aiModel: AIModel,
    val apiUrl: String,
    val modelList: List<String>,
    val apiKey: String,
    val timeout: Long = 30000,
    val maxTokens: Int = 4096,
    val urlEditable: Boolean = false,
) {
    companion object {

        fun getConfig(aiModel: AIModel) = aiGenerationConfigs.first { it.aiModel == aiModel }

        val aiGenerationConfigs = arrayOf(
            // OpenAI
            // https://developers.openai.com/api/docs/models
            AIGenerationConfig(
                aiModel = AIModel.OpenAI,
                apiUrl = "https://api.openai.com/v1/chat/completions",
                modelList = listOf(
                    "gpt-5.2-pro",
                    "gpt-5.2",
                    "gpt-5",
                    "gpt-5-mini",
                    "gpt-5-chat",
                    "gpt-5-nano",
                    "gpt-4.1",
                    "gpt-4.1-mini",
                    "gpt-4.1-nano",
                ),
                apiKey = "",
                maxTokens = 128000
            ),
            // 百度文心一言
            AIGenerationConfig(
                aiModel = AIModel.Ernie,
                apiUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/%s/completions",
                modelList = listOf("ernie-4.0-plus", "ernie-4.0-turbo-2026", "ernie-3.5-128k", "ernie-5.0-preview"),
                apiKey = "",
                maxTokens = 128000
            ),
            // 阿里通义千问
            AIGenerationConfig(
                aiModel = AIModel.QWen,
                apiUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation",
                modelList = listOf(
                    "qwen-2-turbo",
                    "qwen-2-plus",
                    "qwen-2-max",
                    "qwen-2-72b-instruct",
                    "qwen-3-preview"
                ),
                apiKey = "",
                maxTokens = 200000
            ),
            // 字节即梦AI
            AIGenerationConfig(
                aiModel = AIModel.Dream,
                apiUrl = "https://dreamai.bytedance.com/api/v1/chat/%s/completions",
                modelList = listOf("dream-text-v2", "dream-text-pro-2026", "doubao-4.0", "doubao-longcontext"),
                apiKey = "",
                maxTokens = 8192
            ),
            // 火山方舟
            AIGenerationConfig(
                aiModel = AIModel.Volces,
                apiUrl = "https://ark.cn-beijing.volces.com/api/v3/chat/completions",
                modelList = listOf(
                    "doubao-seed-1-6-lite-251015",
                    "doubao-seed-1-6-251015",
                    "doubao-seed-1-6-flash-250828",
                    "doubao-seed-1-6-vision-250815",
                    "doubao-seed-1-8-251228",
                ),
                apiKey = "",
                maxTokens = 8192
            ),
            // 讯飞星火
            AIGenerationConfig(
                aiModel = AIModel.Spark,
                apiUrl = "https://spark-api.xf-yun.com/v4/chat/completions",
                modelList = listOf("spark-4.0", "spark-4.0-turbo", "spark-5.0-preview", "spark-4.0-long"),
                apiKey = "",
                maxTokens = 128000
            ),
            // Google Gemini
            // https://ai.google.dev/gemini-api/docs/pricing?authuser=1
            AIGenerationConfig(
                aiModel = AIModel.Gemini,
                apiUrl = "https://generativelanguage.googleapis.com/v1/models/%s:generateContent",
                modelList = listOf(
                    "gemini-3.1-pro-preview",
                    "gemini-3.1-flash-image-preview",
                    "gemini-3-flash-preview",
                    "gemini-3-pro-image-preview",
                    "gemini-2.5-pro",
                    "gemini-2.5-flash",
                    "gemini-2.5-flash-lite",
                ),
                apiKey = "",
                maxTokens = 1048576
            ),
            // Anthropic Claude
            AIGenerationConfig(
                aiModel = AIModel.Claude,
                apiUrl = "https://api.anthropic.com/v1/messages",
                modelList = listOf("claude-3-5-sonnet-20250129", "claude-3-opus-20250203", "claude-4-preview"),
                apiKey = "",
                maxTokens = 200000
            ),
            // GLM
            // https://docs.bigmodel.cn/cn/guide/models/text
            AIGenerationConfig(
                aiModel = AIModel.GLM,
                apiUrl = "https://open.bigmodel.cn/api/paas/v4/chat/completions",
                modelList = listOf(
                    "glm-5",
                    "glm-4.7",
                    "glm-4.7-flash",
                    "glm-4.7-flashx",
                    "glm-4.6",
                    "glm-4.5-air",
                    "glm-4.5-airx",
                    "glm-4.5-flash",
                    "glm-4-flash-250414",
                    "glm-4-flashx-250414"
                ),
                apiKey = "",
                maxTokens = 128000
            ),
            // DeepSeek
            AIGenerationConfig(
                aiModel = AIModel.DeepSeek,
                apiUrl = "https://api.deepseek.com/v1/chat/completions",
                modelList = listOf("deepseek-chat", "deepseek-r1", "deepseek-vl"),
                apiKey = "",
                maxTokens = 128000
            ),
            // Custom
            AIGenerationConfig(
                aiModel = AIModel.Custom,
                apiUrl = "",
                modelList = emptyList<String>(),
                apiKey = "",
                maxTokens = 128000,
                urlEditable = true,
            ),
        )
    }
}
