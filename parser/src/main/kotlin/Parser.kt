package org.semgusng.parser

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.semgusng.parser.event.ParseEvent

/**
 * Format for (de)serializing [ParseEvent]s.
 */
@OptIn(ExperimentalSerializationApi::class) val format = Json {
  ignoreUnknownKeys = true
  isLenient = true
  encodeDefaults = true
  prettyPrint = true
  explicitNulls = false
}

fun <T> String.decode(serializer: KSerializer<T>) = format.decodeFromString(serializer, this)

fun <T> String.decodeBatch(serializer: KSerializer<T>) = format.decodeFromString(ListSerializer(serializer), this)

fun <T> T.encode(serializer: KSerializer<T>) = format.encodeToString(serializer, this)

fun String.decodeJsonArr() = decodeBatch(JsonObject.serializer()).map { it.toString() }
