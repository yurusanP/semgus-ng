package org.semgusng.parser.model.smt

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import org.semgusng.parser.model.smt.SmtSortIdentifier.Simple

@Serializable(with = SmtSortIdentifierSerializer::class)
sealed class SmtSortIdentifier {
  abstract val name: SmtIdentifier

  @Serializable(with = SmtSortIdentifierSerializer.SimpleSerializer::class)
  data class Simple(override val name: SmtIdentifier) : SmtSortIdentifier()

  // TODO: Composite
}

object SmtSortIdentifierSerializer : JsonContentPolymorphicSerializer<SmtSortIdentifier>(SmtSortIdentifier::class) {
  override fun selectDeserializer(element: kotlinx.serialization.json.JsonElement): kotlinx.serialization.DeserializationStrategy<out SmtSortIdentifier> {
    return Simple.serializer()
  }

  object SimpleSerializer : KSerializer<Simple> {
    override val descriptor: SerialDescriptor
      get() = PrimitiveSerialDescriptor("SmtSortIdentifier.Simple", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Simple {
      return Simple(decoder.decodeSerializableValue(SmtIdentifier.Simple.serializer()))
    }

    override fun serialize(encoder: Encoder, value: Simple) {
      encoder.encodeString(value.name.toString())
    }
  }
}
