package org.semgusng.parser.smt

import kotlinx.serialization.Serializable

@Serializable
data class SmtAttribute(
  val keyword: SmtKeyword,
  val value: SmtAttributeValue,
)

@Serializable
data class SmtKeyword(
  val name: String,
) {
  override fun toString(): String {
    return ":$name"
  }
}
