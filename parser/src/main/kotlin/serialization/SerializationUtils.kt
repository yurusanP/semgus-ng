package org.semgusng.parser.serialization

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.semgusng.parser.event.ParseEvent
import org.semgusng.parser.event.ParseEventSerializer

/**
 * Format for (de)serializing [ParseEvent]s.
 */
val format = Json {
  ignoreUnknownKeys = true
  isLenient = true
  encodeDefaults = true
  prettyPrint = true
  encodeDefaults = true
}

/**
 * Decode a [ParseEvent] from a Json [String].
 * @receiver the Json [String] to decode
 * @return the decoded [ParseEvent]
 */
fun String.decode() = format.decodeFromString(ParseEventSerializer, this)

fun String.decodeBatch() = format.decodeFromString(ListSerializer(ParseEventSerializer), this)

/**
 * Encode a [ParseEvent] to a Json [String].
 * @receiver the [ParseEvent] to encode
 * @return the encoded Json [String]
 */
fun ParseEvent.encode() = format.encodeToString(ParseEventSerializer, this)

fun List<ParseEvent>.encodeBatch() = format.encodeToString(ListSerializer(ParseEventSerializer), this)
