package org.semgusng.parser.smt

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import org.semgusng.parser.smt.SmtSortIdentifier.Simple

@Serializable(with = SmtSortIdentifierSerializer::class)
sealed class SmtSortIdentifier {
  abstract val name: SmtIdentifier

  @Serializable(with = SmtSortIdentifierSerializer.SimpleSerializer::class)
  data class Simple(override val name: SmtIdentifier) : SmtSortIdentifier()

  // Note: Parameterized sorts not yet supported by the JSON serializer.
  // See SemgusParser/Json/Converters/SmtSortIdentifierConverter.cs
}

object SmtSortIdentifierSerializer : JsonContentPolymorphicSerializer<SmtSortIdentifier>(SmtSortIdentifier::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out SmtSortIdentifier> {
    return Simple.serializer()
  }

  object SimpleSerializer : KSerializer<Simple> {
    override val descriptor: SerialDescriptor
      get() = PrimitiveSerialDescriptor("SmtSortIdentifier.Simple", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Simple {
      // that simple is not this simple
      return Simple(decoder.decodeSerializableValue(SmtIdentifier.Simple.serializer()))
    }

    override fun serialize(encoder: Encoder, value: Simple) {
      encoder.encodeString(value.name.toString())
    }
  }
}
