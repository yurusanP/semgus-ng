package org.semgusng.parser.model.smt

import kotlinx.serialization.Serializable

@Serializable
data class SmtFunctionRank(val returnSort: SmtSort, val argumentSorts: List<SmtSort>)
