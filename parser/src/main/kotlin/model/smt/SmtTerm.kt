package org.semgusng.parser.model.smt

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = SmtTermSerializer::class)
sealed class SmtTerm {
  @Suppress("unused")
  abstract val `$termType`: String

  @Serializable
  data class SmtVariable(
    val name: String,
    val sort: SmtSort,
    override val `$termType`: String = "variable",
  ) : SmtTerm()

  @Serializable
  data class SmtLambdaBinder(
    val arguments: List<SmtIdentifier>,
    val body: SmtTerm,
    override val `$termType`: String = "lambda",
  ) : SmtTerm()
}

object SmtTermSerializer : JsonContentPolymorphicSerializer<SmtTerm>(SmtTerm::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out SmtTerm> {
    return when (val termTypeContent = element.jsonObject["\$termType"]?.jsonPrimitive?.content) {
      "variable" -> SmtTerm.SmtVariable.serializer()
      "lambda" -> SmtTerm.SmtLambdaBinder.serializer()
      else -> error("Unknown term type: $termTypeContent")
    }
  }
}
