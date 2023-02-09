package org.semgusng.parser.smt

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.semgusng.parser.smt.SmtAttributeValue.AList
import org.semgusng.parser.smt.SmtAttributeValue.AString

@Serializable(with = SmtAttributeValueSerializer::class)
sealed class SmtAttributeValue {
  @Serializable(with = SmtAttributeValueSerializer.AStringSerializer::class)
  data class AString(val value: String) : SmtAttributeValue() {
    override fun toString(): String {
      return value
    }
  }

  @Serializable(with = SmtAttributeValueSerializer.AListSerializer::class)
  data class AList(val values: List<SmtAttributeValue>) : SmtAttributeValue() {
    override fun toString(): String {
      return values.joinToString(", ", "(", ")")
    }
  }
}

object SmtAttributeValueSerializer : JsonContentPolymorphicSerializer<SmtAttributeValue>(SmtAttributeValue::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out SmtAttributeValue> {
    return when (element) {
      is JsonPrimitive -> AString.serializer()
      is JsonArray -> AList.serializer()
      else -> throw IllegalArgumentException("Unknown SmtAttributeValue: $element")
    }
  }

  object AStringSerializer : KSerializer<AString> {
    override val descriptor: SerialDescriptor
      get() = PrimitiveSerialDescriptor("SmtAttributeValue.AString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AString {
      return AString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: AString) {
      encoder.encodeString(value.value)
    }
  }

  object AListSerializer : KSerializer<AList> {
    override val descriptor: SerialDescriptor
      get() = ListSerializer(SmtAttributeValue.serializer()).descriptor

    override fun deserialize(decoder: Decoder): AList {
      return AList(decoder.decodeSerializableValue(ListSerializer(SmtAttributeValue.serializer())))
    }

    override fun serialize(encoder: Encoder, value: AList) {
      encoder.encodeSerializableValue(ListSerializer(SmtAttributeValue.serializer()), value.values)
    }
  }
}
