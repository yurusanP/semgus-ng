package org.semgusng.pretty.render

import org.semgusng.pretty.base.Code
import org.semgusng.pretty.base.SimpleCode
import org.semgusng.pretty.base.best

/**
 * Layouts the [String] representation of pretty [Code].
 */
fun Code.stringPretty(w: Int) = this.best(w).layout()

private fun List<SimpleCode>.layout(): String =
  this.joinToString("", transform = SimpleCode::layout)

private fun SimpleCode.layout(): String = when (this) {
  is SimpleCode.Line -> "\n${" ".repeat(this.i)}${this.xs.layout()}"
  is SimpleCode.CPlain -> this.c.toString()
  is SimpleCode.SPlain -> this.s
}
