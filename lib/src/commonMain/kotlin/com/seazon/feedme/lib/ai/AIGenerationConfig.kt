package com.seazon.feedme.lib.ai

enum class AIModel {
    Gemini,
    Volces,
    GLM,
    QWen,
    OpenAI,
    DeepSeek,
    MiniMax,
    Claude,
    Ernie,
    Dream,
    Spark,
    Custom,
}

data class AIGenerationConfig(
    val aiModel: AIModel,
    val apiUrl: String,
    val modelList: List<String>,
    val apiKey: String = "",
    val timeout: Long = 30000,
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
                    "gpt-4.1-nano",
                    "gpt-5-mini",
                    "gpt-5.4",
                    "gpt-5.2",
                    "gpt-5",
                    "gpt-5-chat",
                    "gpt-5-nano",
                    "gpt-4.1",
                    "gpt-4.1-mini",
                ),
            ),
            // 百度文心一言
            AIGenerationConfig(
                aiModel = AIModel.Ernie,
//                apiUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions",
                apiUrl = "https://qianfan.baidubce.com/v2/chat/completions",
                modelList = listOf(
                    "ernie-5.0-thinking-preview",
                    "ernie-4.0-plus",
                    "ernie-4.0-turbo-2026",
                    "ernie-4.5-turbo-128k-preview",
                    "ernie-4.5-turbo-32k",
                    "ernie-4.5-turbo-128k",
                    "ernie-4.5-0.3b",
                    "ernie-4.5-21b-a3b",
                    "ernie-4.5-vl-28b-a3b",
                    "ernie-3.5-128k",
                ),
            ),
            // 阿里通义千问
            AIGenerationConfig(
                aiModel = AIModel.QWen,
                apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
//                apiUrl = "https://dashscope-intl.aliyuncs.com/compatible-mode/v1/chat/completions",
//                apiUrl = "https://dashscope-us.aliyuncs.com/compatible-mode/v1/chat/completions",
                modelList = listOf(
                    "qwen-flash",
                    "qwen3.5-plus",
                    "qwen3.5-flash",
                    "qwen3.5-27b",
                    "qwen3-max",
                    "qwen-plus",
                ),
            ),
            // MiniMax
            // https://platform.minimax.io/docs/guides/models-intro
            AIGenerationConfig(
                aiModel = AIModel.MiniMax,
                apiUrl = "https://api.minimax.io/v1/text/chatcompletion_v2",
                modelList = listOf(
                    "MiniMax-M2.5",
                    "MiniMax-M2.5-highspeed",
                    "MiniMax-M2.1",
                    "MiniMax-M2.1-highspeed",
                    "MiniMax-M2",
                    "M2-her",
                ),
            ),
            // 字节即梦AI
            AIGenerationConfig(
                aiModel = AIModel.Dream,
                apiUrl = "https://dreamai.bytedance.com/api/v1/chat/%s/completions",
                modelList = listOf("dream-text-v2", "dream-text-pro-2026", "doubao-4.0", "doubao-longcontext"),
            ),
            // 火山方舟
            AIGenerationConfig(
                aiModel = AIModel.Volces,
                apiUrl = "https://ark.cn-beijing.volces.com/api/v3/chat/completions",
                modelList = listOf(
                    "doubao-seed-2-0-mini-260215",
                    "doubao-seed-2-0-pro-260215",
                    "doubao-seed-2-0-lite-260215",
                    "doubao-seed-1-8-251228",
                    "doubao-seed-1-6-251015",
                    "doubao-seed-1-6-lite-251015",
                    "doubao-seed-1-6-flash-250828",
                ),
            ),
            // 讯飞星火
            AIGenerationConfig(
                aiModel = AIModel.Spark,
                apiUrl = "https://spark-api.xf-yun.com/v4/chat/completions",
                modelList = listOf("spark-4.0", "spark-4.0-turbo", "spark-5.0-preview", "spark-4.0-long"),
            ),
            // Google Gemini
            // https://ai.google.dev/gemini-api/docs/pricing?authuser=1
            AIGenerationConfig(
                aiModel = AIModel.Gemini,
                apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent",
                modelList = listOf(
                    "gemini-2.5-flash-lite",
                    "gemini-3.1-pro-preview",
                    "gemini-3.1-flash-lite",
                    "gemini-3-flash-preview",
                    "gemini-2.5-pro",
                    "gemini-2.5-flash",
                ),
            ),
            // Anthropic Claude
            AIGenerationConfig(
                aiModel = AIModel.Claude,
                apiUrl = "https://api.anthropic.com/v1/messages",
                modelList = listOf(
                    "claude-3-5-haiku-latest",
                    "claude-4-preview",
                    "claude-3-5-sonnet-20250129",
                    "claude-3-opus-20250203",
                ),
            ),
            // GLM
            // https://docs.bigmodel.cn/cn/guide/models/text
            AIGenerationConfig(
                aiModel = AIModel.GLM,
                apiUrl = "https://open.bigmodel.cn/api/paas/v4/chat/completions",
                modelList = listOf(
                    "glm-4.7-flash",
                    "glm-5",
                    "glm-4.7",
                    "glm-4.7-flashx",
                    "glm-4.6",
                    "glm-4.5-air",
                    "glm-4.5-airx",
                    "glm-4.5-flash",
                    "glm-4-flashx-250414",
                    "glm-4-flash-250414",
                ),
            ),
            // DeepSeek
            AIGenerationConfig(
                aiModel = AIModel.DeepSeek,
                apiUrl = "https://api.deepseek.com/v1/chat/completions",
                modelList = listOf(
                    "deepseek-chat",
                    "deepseek-v3.2",
                    "deepseek-v3.1",
                    "deepseek-v3",
                    "deepseek-r1",
                ),
            ),
            // Custom
            AIGenerationConfig(
                aiModel = AIModel.Custom,
                apiUrl = "",
                modelList = emptyList(),
                urlEditable = true,
            ),
        )
    }
}
