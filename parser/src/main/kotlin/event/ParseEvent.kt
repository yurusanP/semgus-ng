package org.semgusng.parser.event

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.semgusng.parser.event.ParseEvent.*
import org.semgusng.parser.model.smt.SmtAttributeValue
import org.semgusng.parser.model.smt.SmtFunctionRank
import org.semgusng.parser.model.smt.SmtIdentifier
import org.semgusng.parser.model.smt.SmtSortIdentifier

@Serializable
sealed class ParseEvent {
  abstract val `$event`: String
  abstract val `$type`: String

  @Serializable
  data class SetInfoEvent(
    val keyword: String,
    val value: SmtAttributeValue,
    override val `$event`: String = "set-info",
    override val `$type`: String = "meta",
  ) : ParseEvent()

  @Serializable
  data class TermTypeDeclarationEvent(
    val name: String,
    override val `$event`: String = "declare-term-type",
    override val `$type`: String = "semgus",
  ) : ParseEvent()

  @Serializable
  data class TermTypeDefinitionEvent(
    val name: String,
    val constructors: List<Constructor>,
    override val `$event`: String = "define-term-type",
    override val `$type`: String = "semgus",
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
    override val `$event`: String = "declare-function",
    override val `$type`: String = "smt",
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
      else -> throw IllegalArgumentException("Unknown event: $eventContent")
    }
  }
}
