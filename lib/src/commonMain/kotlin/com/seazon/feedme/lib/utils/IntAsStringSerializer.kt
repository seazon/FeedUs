package com.seazon.feedme.lib.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

object IntAsStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IntAsString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        require(decoder is JsonDecoder) { "This decoder should be a JsonDecoder" }
        val jsonElement = decoder.decodeJsonElement()
        return when (jsonElement) {
            is JsonPrimitive -> {
                jsonElement.intOrNull?.toString() ?: jsonElement.content
            }

            else -> throw SerializationException("Expected a JsonPrimitive for IntAsString")
        }
    }
}