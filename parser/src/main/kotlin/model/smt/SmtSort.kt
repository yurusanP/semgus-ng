package org.semgusng.parser.model.smt

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SmtSortSerializer::class)
data class SmtSort(val name: SmtSortIdentifier)

class SmtSortSerializer : KSerializer<SmtSort> {
  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("SmtSortIdentifier.Simple", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): SmtSort {
    return SmtSort(decoder.decodeSerializableValue(SmtSortIdentifier.serializer()))
  }

  override fun serialize(encoder: Encoder, value: SmtSort) {
    encoder.encodeSerializableValue(SmtSortIdentifier.serializer(), value.name)
  }
}
