package org.semgusng.parser.model.smt

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import org.semgusng.parser.model.smt.SmtTerm.*

@Serializable(with = SmtTermSerializer::class)
sealed class SmtTerm {
  @SerialName("\$termType") abstract val termType: String

  @Serializable(with = SmtTermSerializer.SmtNumeralLiteralSerializer::class)
  data class SmtNumeralLiteral(
    val value: Long,
    @SerialName("\$termType") override val termType: String = "numeral",
  ) : SmtTerm()

  @Serializable(with = SmtTermSerializer.SmtDecimalLiteralSerializer::class)
  data class SmtDecimalLiteral(
    val value: Double,
    @SerialName("\$termType") override val termType: String = "decimal",
  ) : SmtTerm()

  @Serializable(with = SmtTermSerializer.SmtStringLiteralSerializer::class)
  data class SmtStringLiteral(
    val value: String,
    @SerialName("\$termType") override val termType: String = "string",
  ) : SmtTerm()

  @Serializable
  data class SmtVariable(
    val name: String,
    val sort: SmtSort,
    @SerialName("\$termType") override val termType: String = "variable",
  ) : SmtTerm()

  @Serializable
  data class SmtLambdaBinder(
    val arguments: List<SmtIdentifier>,
    val body: SmtTerm,
    @SerialName("\$termType") override val termType: String = "lambda",
  ) : SmtTerm()
}

object SmtTermSerializer : JsonContentPolymorphicSerializer<SmtTerm>(SmtTerm::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out SmtTerm> {
    if (element is JsonPrimitive) {
      return when {
        element.longOrNull != null -> SmtNumeralLiteral.serializer()
        element.doubleOrNull != null -> SmtDecimalLiteral.serializer()
        else -> SmtStringLiteral.serializer()
      }
    }

    return when (val termTypeContent = element.jsonObject["\$termType"]?.jsonPrimitive.toString()) {
      "variable" -> SmtVariable.serializer()
      "lambda" -> SmtLambdaBinder.serializer()
      else -> error("Unknown term type: $termTypeContent")
    }
  }

  object SmtNumeralLiteralSerializer : KSerializer<SmtNumeralLiteral> {
    override val descriptor = PrimitiveSerialDescriptor("SmtTerm.SmtNumeralLiteral", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): SmtNumeralLiteral {
      return SmtNumeralLiteral(decoder.decodeLong())
    }

    override fun serialize(encoder: Encoder, value: SmtNumeralLiteral) {
      encoder.encodeLong(value.value)
    }
  }

  object SmtDecimalLiteralSerializer : KSerializer<SmtDecimalLiteral> {
    override val descriptor = PrimitiveSerialDescriptor("SmtTerm.SmtDecimalLiteral", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): SmtDecimalLiteral {
      return SmtDecimalLiteral(decoder.decodeDouble())
    }

    override fun serialize(encoder: Encoder, value: SmtDecimalLiteral) {
      encoder.encodeDouble(value.value)
    }
  }

  object SmtStringLiteralSerializer : KSerializer<SmtStringLiteral> {
    override val descriptor = PrimitiveSerialDescriptor("SmtTerm.SmtStringLiteral", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): SmtStringLiteral {
      return SmtStringLiteral(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: SmtStringLiteral) {
      encoder.encodeString(value.value)
    }
  }
}
