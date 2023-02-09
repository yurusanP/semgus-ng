package org.semgusng.parser.smt

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import org.semgusng.parser.smt.SmtIdentifier.Simple

@Serializable(with = SmtIdentifierSerializer::class)
sealed class SmtIdentifier {
  abstract val symbol: String

  @Serializable(with = SmtIdentifierSerializer.SimpleSerializer::class)
  data class Simple(override val symbol: String) : SmtIdentifier() {
    override fun toString(): String {
      return symbol
    }
  }

  // Todo: Add support for indexed identifiers

  //  @Serializable(with = SmtIdentifierSerializer.CompositeSerializer::class)
  //  data class Composite(override var symbol: String, var indices: List<Index>) : SmtIdentifier() {
  //    @Serializable
  //    sealed class Index {
  //      @Serializable
  //      data class NString(val value: String) : Index()
  //      @Serializable
  //      data class NInt(val value: Int) : Index()
  //    }
  //  }
}

object SmtIdentifierSerializer : JsonContentPolymorphicSerializer<SmtIdentifier>(SmtIdentifier::class) {
  override fun selectDeserializer(element: kotlinx.serialization.json.JsonElement): kotlinx.serialization.DeserializationStrategy<out SmtIdentifier> {
    return Simple.serializer()
  }

  object SimpleSerializer : KSerializer<Simple> {
    override val descriptor: SerialDescriptor
      get() = PrimitiveSerialDescriptor("SmtIdentifier.Simple", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Simple {
      return Simple(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Simple) {
      encoder.encodeString(value.symbol)
    }
  }
}
