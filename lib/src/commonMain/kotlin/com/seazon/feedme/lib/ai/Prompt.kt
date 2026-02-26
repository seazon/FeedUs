package com.seazon.feedme.lib.ai

enum class PromptType {
    Summary, Translate
}

data class Prompt(
    val type: PromptType,
    val content: String,
) {
    companion object {
        val configs = arrayOf(
            Prompt(
                PromptType.Summary,
                """
Summarize the article in %s within 100 words.
Keep it concise and complete.
Use markdown to improve readability if need.
Output only the result directly, no extra explanations.
Article: %s""".trimIndent()
            ),
            Prompt(
                PromptType.Translate,
                """
Translate the following text into %s.
Keep the original meaning, tone, and logic completely unchanged.
Use natural, fluent, and accurate expressions.
Do not add extra explanations or comments.
Preserve formatting, paragraphs, and key terms.
Output only the translated result.
Text to translate:%s""".trimMargin()
            ),
        )
    }
}