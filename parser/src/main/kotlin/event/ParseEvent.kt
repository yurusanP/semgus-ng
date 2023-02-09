package org.semgusng.parser.event

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.semgusng.parser.chc.SemanticRelation
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
    val name: SmtSortIdentifier,
    @SerialName("\$event") override val event: String = "declare-term-type",
    @SerialName("\$type") override val type: String = "semgus",
  ) : ParseEvent()

  @Serializable
  data class TermTypeDefinitionEvent(
    val name: SmtSortIdentifier,
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

  @Serializable
  data class ChcEvent(
    val head: SemanticRelation,
    val bodyRelations: List<SemanticRelation>,
    val inputVariables: List<SmtIdentifier>,
    val outputVariables: List<SmtIdentifier>,
    val variables: List<SmtIdentifier>,
    val constraint: SmtTerm,
    val constructor: Constructor,
    @SerialName("\$event") override val event: String = "chc",
    @SerialName("\$type") override val type: String = "semgus",
  ) : ParseEvent() {
    @Serializable
    data class Constructor(
      val name: SmtIdentifier,
      val arguments: List<SmtIdentifier>,
      val argumentSorts: List<SmtSortIdentifier>,
      val returnSort: SmtSortIdentifier,
    )
  }

  @Serializable
  data class SynthFunEvent(
    val name: SmtIdentifier,
    // Hack: Shouldn't it be SmtSortIdentifier?
    val termType: SmtIdentifier,
    val grammar: Grammar,
    @SerialName("\$event") override val event: String = "synth-fun",
    @SerialName("\$type") override val type: String = "semgus",
  ) : ParseEvent() {
    @Serializable
    data class NonTerminalDeclaration(
      val name: SmtIdentifier,
      val termType: SmtIdentifier,
    )

    @Serializable
    data class Production(
      val instance: SmtIdentifier,
      val operator: SmtIdentifier,
      val occurrences: List<SmtIdentifier>,
    )

    @Serializable
    data class Grammar(
      val nonTerminals: List<NonTerminalDeclaration>,
      val productions: List<Production>,
    )
  }

  @Serializable
  data class ConstraintEvent(
    val constraint: SmtTerm,
    @SerialName("\$event") override val event: String = "constraint",
    @SerialName("\$type") override val type: String = "semgus",
  ) : ParseEvent()

  @Serializable
  data class CheckSynthEvent(
    @SerialName("\$event") override val event: String = "check-synth",
    @SerialName("\$type") override val type: String = "semgus",
  ) : ParseEvent()

  @Serializable
  data class EndOfStreamEvent(
    @SerialName("\$event") override val event: String = "end-of-stream",
    @SerialName("\$type") override val type: String = "meta",
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
      "chc" -> ChcEvent.serializer()
      "synth-fun" -> SynthFunEvent.serializer()
      "constraint" -> ConstraintEvent.serializer()
      "check-synth" -> CheckSynthEvent.serializer()
      "end-of-stream" -> EndOfStreamEvent.serializer()
      else -> error("Unknown event: $eventContent")
    }
  }
}
