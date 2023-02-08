package org.semgusng.parser.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.semgusng.parser.event.ParseEvent

/**
 * Format for (de)serializing [ParseEvent]s.
 */
val format = Json {
  ignoreUnknownKeys = true
  isLenient = true
  encodeDefaults = true
  prettyPrint = true
}

fun <T> String.decode(serializer: KSerializer<T>) = format.decodeFromString(serializer, this)

fun <T> String.decodeBatch(serializer: KSerializer<T>) = this.decode(ListSerializer(serializer))

fun <T> T.encode(serializer: KSerializer<T>) = format.encodeToString(serializer, this)

fun <T> List<T>.encodeBatch(serializer: KSerializer<T>) = this.encode(ListSerializer(serializer))
