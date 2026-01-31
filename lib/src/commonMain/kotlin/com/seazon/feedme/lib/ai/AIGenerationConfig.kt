package com.seazon.feedme.lib.ai

enum class AIModel {
    OpenAI, Ernie, QWen, Dream, Volces, Spark, Gemini, Claude, GLM
}

data class AIGenerationConfig(
    val aiModel: AIModel,
    val apiUrl: String,
    val modelList: List<String>,
    val apiKey: String,
    val timeout: Long = 30000,
    val maxTokens: Int = 4096
) {
    companion object {

        fun getConfig(aiModel: AIModel) = aiGenerationConfigs.first { it.aiModel == aiModel }

        val aiGenerationConfigs = arrayOf(
            // OpenAI
            AIGenerationConfig(
                aiModel = AIModel.OpenAI,
                apiUrl = "https://api.openai.com/v1/chat/completions", // 通用接口，模型在请求体中指定，无需URL占位符
                modelList = listOf("gpt-3.5-turbo-0125", "gpt-4o", "gpt-4o-mini", "gpt-4o-ultra", "gpt-4-turbo-2025"),
                apiKey = "",
                maxTokens = 128000 // gpt-4o ultra支持128k tokens
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
                apiUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation", // 通用接口，模型在请求体指定
                modelList = listOf(
                    "qwen-2-turbo",
                    "qwen-2-plus",
                    "qwen-2-max",
                    "qwen-2-72b-instruct",
                    "qwen-3-preview"
                ),
                apiKey = "",
                maxTokens = 200000 // 千问2代支持20万tokens
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
                apiUrl = "https://spark-api.xf-yun.com/v4/chat/completions", // v4版本，模型在请求体指定
                modelList = listOf("spark-4.0", "spark-4.0-turbo", "spark-5.0-preview", "spark-4.0-long"),
                apiKey = "",
                maxTokens = 128000
            ),
            // Google Gemini
            AIGenerationConfig(
                aiModel = AIModel.Gemini,
                apiUrl = "https://generativelanguage.googleapis.com/v1/models/%s:generateContent",
                modelList = listOf(
                    "gemini-2.5-flash",
                    "gemini-2.5-flash-lite",
                    "gemini-2.5-pro",
                    "gemini-3-flash-preview",
                    "gemini-3-pro-preview",
                ),
                apiKey = "",
                maxTokens = 1048576 // Gemini 3.0 Ultra支持100万+ tokens
            ),
            // Anthropic Claude
            AIGenerationConfig(
                aiModel = AIModel.Claude,
                apiUrl = "https://api.anthropic.com/v1/messages", // 通用接口，模型在请求体指定
                modelList = listOf("claude-3-5-sonnet-20250129", "claude-3-opus-20250203", "claude-4-preview"),
                apiKey = "",
                maxTokens = 200000
            ),
            // 智谱AI
            AIGenerationConfig(
                aiModel = AIModel.GLM,
                apiUrl = "https://open.bigmodel.cn/api/paas/v4/chat/%s/completions",
                modelList = listOf("glm-4-flash", "glm-4-plus", "glm-4-9b", "glm-5-preview"),
                apiKey = "",
                maxTokens = 128000
            )
        )
    }
}
