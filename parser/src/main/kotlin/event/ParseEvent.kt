package org.semgusng.parser.event

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.semgusng.parser.event.ParseEvent.*
import org.semgusng.parser.smt.*

@Serializable(with = ParseEventSerializer::class)
sealed class ParseEvent {
  @SerialName("\$event") abstract val event: String

  @SerialName("\$type") abstract val type: String

  @Serializable
  data class SetInfoEvent(
    val keyword: String,
    val value: SmtAttributeValue,
    @SerialName("\$event") override val event: String = "set-info",
    @SerialName("\$type") override val type: String = "meta",
  ) : ParseEvent()

  @Serializable
  data class TermTypeDeclarationEvent(
    val name: String,
    @SerialName("\$event") override val event: String = "declare-term-type",
    @SerialName("\$type") override val type: String = "semgus",
  ) : ParseEvent()

  @Serializable
  data class TermTypeDefinitionEvent(
    val name: String,
    val constructors: List<Constructor>,
    @SerialName("\$event") override val event: String = "define-term-type",
    @SerialName("\$type") override val type: String = "semgus",
  ) : ParseEvent() {
    @Serializable
    data class Constructor(
      val name: SmtIdentifier,
      val children: List<SmtSortIdentifier>,
    )
  }

  @Serializable
  data class FunctionDeclarationEvent(
    val name: SmtIdentifier,
    val rank: SmtFunctionRank,
    @SerialName("\$event") override val event: String = "declare-function",
    @SerialName("\$type") override val type: String = "smt",
  ) : ParseEvent()

  @Serializable
  data class FunctionDefinitionEvent(
    val name: SmtIdentifier,
    val rank: SmtFunctionRank,
    val definition: SmtTerm.SmtLambdaBinder,
    @SerialName("\$event") override val event: String = "define-function",
    @SerialName("\$type") override val type: String = "smt",
  ) : ParseEvent()
}

/**
 * A polymorphic serializer for [ParseEvent]s.
 */
object ParseEventSerializer : JsonContentPolymorphicSerializer<ParseEvent>(ParseEvent::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ParseEvent> {
    return when (val eventContent = element.jsonObject["\$event"]?.jsonPrimitive?.content) {
      "set-info" -> SetInfoEvent.serializer()
      "declare-term-type" -> TermTypeDeclarationEvent.serializer()
      "define-term-type" -> TermTypeDefinitionEvent.serializer()
      "declare-function" -> FunctionDeclarationEvent.serializer()
      "define-function" -> FunctionDefinitionEvent.serializer()
      else -> error("Unknown event: $eventContent")
    }
  }
}
