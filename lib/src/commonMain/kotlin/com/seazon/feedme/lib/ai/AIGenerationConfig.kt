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
                    "gpt-5.4-nano-2026-03-17", // $0.2•$1.25
                    "gpt-5.6-luna", // $1•$6
                ),
            ),
            // 百度文心一言
            AIGenerationConfig(
                aiModel = AIModel.Ernie,
//                apiUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions",
                apiUrl = "https://qianfan.baidubce.com/v2/chat/completions",
                modelList = listOf(
                    "ernie-4.5-turbo-20260402", // ¥0.8/3.2
                    "ernie-5.1", // ¥4/18
                ),
            ),
            // 阿里通义千问
            AIGenerationConfig(
                aiModel = AIModel.QWen,
                apiUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions",
//                apiUrl = "https://dashscope-intl.aliyuncs.com/compatible-mode/v1/chat/completions",
//                apiUrl = "https://dashscope-us.aliyuncs.com/compatible-mode/v1/chat/completions",
                modelList = listOf(
                    "qwen3.7-flash", // ¥0.2/0.8
                    "qwen3.7-max", // ¥12/36
                ),
            ),
            // MiniMax
            // https://platform.minimax.io/docs/guides/models-intro
            AIGenerationConfig(
                aiModel = AIModel.MiniMax,
                apiUrl = "https://api.minimax.io/v1/text/chatcompletion_v2",
                modelList = listOf(
                    "MiniMax-M2.7", // ¥0.3/1.2
                    "MiniMax-M3", // ¥0.6/2.4
                ),
            ),
            // 字节即梦AI
            AIGenerationConfig(
                aiModel = AIModel.Dream,
                apiUrl = "https://dreamai.bytedance.com/api/v1/chat/%s/completions",
                modelList = listOf("dream-text-v2", "doubao-4.0"),
            ),
            // 火山方舟
            AIGenerationConfig(
                aiModel = AIModel.Volces,
                apiUrl = "https://ark.cn-beijing.volces.com/api/v3/chat/completions",
                modelList = listOf(
                    "doubao-seed-2-0-mini-260215",
                    "doubao-seed-2-0-pro-260215",
                ),
            ),
            // 讯飞星火
            AIGenerationConfig(
                aiModel = AIModel.Spark,
                apiUrl = "https://spark-api.xf-yun.com/v4/chat/completions",
                modelList = listOf("spark-4.0-turbo", "spark-5.0-preview"),
            ),
            // Google Gemini
            // https://ai.google.dev/gemini-api/docs/pricing?authuser=1
            AIGenerationConfig(
                aiModel = AIModel.Gemini,
                apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent",
                modelList = listOf(
                    "gemini-3.1-flash-lite",
                    "gemini-3.1-pro-preview",
                ),
            ),
            // Anthropic Claude
            AIGenerationConfig(
                aiModel = AIModel.Claude,
                apiUrl = "https://api.anthropic.com/v1/messages",
                modelList = listOf(
                    "claude-3-5-haiku-latest",
                    "claude-4-preview",
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
                ),
            ),
            // DeepSeek
            AIGenerationConfig(
                aiModel = AIModel.DeepSeek,
                apiUrl = "https://api.deepseek.com/v1/chat/completions",
                modelList = listOf(
                    "deepseek-chat",
                    "deepseek-v3.2",
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
