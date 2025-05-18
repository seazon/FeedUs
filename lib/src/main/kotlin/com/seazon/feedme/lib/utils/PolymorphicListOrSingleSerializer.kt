package com.seazon.feedme.lib.utils

import com.seazon.feedme.lib.rss.service.ttrss.bo.TtrssItem
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject

class PolymorphicListOrSingleSerializer : KSerializer<List<TtrssItem>> {
    override val descriptor: SerialDescriptor = ListSerializer(TtrssItem.serializer()).descriptor

    override fun deserialize(decoder: Decoder): List<TtrssItem> {
        require(decoder is JsonDecoder) { "This decoder should be a JsonDecoder" }
        return when (decoder.decodeJsonElement()) {
            is JsonArray -> Json.decodeFromJsonElement(ListSerializer(TtrssItem.serializer()), decoder.decodeJsonElement())
            is JsonObject -> listOf()
            else -> throw SerializationException("Unexpected JSON type for problematicField")
        }
    }

    override fun serialize(encoder: Encoder, value: List<TtrssItem>) {
        val jsonElement = Json.encodeToJsonElement(ListSerializer(TtrssItem.serializer()), value)
        when (jsonElement) {
            is JsonArray -> {
                encoder.encodeSerializableValue(ListSerializer(TtrssItem.serializer()), value)
            }

            else -> throw SerializationException("Unexpected state during serialization")
        }
    }
}