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
                "Summary the text in %s, JSON format output, key is dst, dst should be a string, and no more than 400 words, use markdown to improve readability if need. Text: %s"
            ),
            Prompt(
                PromptType.Translate,
                "Translate to %s, JSON format output, key is dst. Text: %s"
            ),
        )
    }
}