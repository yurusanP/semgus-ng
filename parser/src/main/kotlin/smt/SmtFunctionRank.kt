package org.semgusng.parser.smt

import kotlinx.serialization.Serializable

@Serializable
data class SmtFunctionRank(val returnSort: SmtSortIdentifier, val argumentSorts: List<SmtSortIdentifier>)
