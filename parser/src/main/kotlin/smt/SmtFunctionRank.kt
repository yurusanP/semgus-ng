package org.semgusng.parser.smt

import kotlinx.serialization.Serializable

@Serializable
data class SmtFunctionRank(val returnSort: SmtSort, val argumentSorts: List<SmtSort>)
