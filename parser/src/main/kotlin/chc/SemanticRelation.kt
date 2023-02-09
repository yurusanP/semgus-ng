package org.semgusng.parser.chc

import kotlinx.serialization.Serializable

@Serializable
data class SemanticRelation(
  val name: String,
  val signature: List<String>,
  val arguments: List<String>,
)
