package org.semgusng.pretty.base

import org.semgusng.pretty.Pretty
import org.semgusng.pretty.render.stringPretty

/**
 * [Code] should of course be pretty.
 */
sealed class Code : Pretty {
  /**
   * Codify for free!
   */
  override fun codify(): Code = when (this) {
    is Fail -> plain("Fail")
    is Empty -> plain("Empty")
    is Line -> plain("Line")
    is CPlain -> plain("CPlain(\'${this.c}\')")
    is SPlain -> plain("SPlain(\"${this.s}\")")
    is FlatAlt -> plain("FlatAlt(${this.default.codify().stringPretty(0)}, ${this.alt.codify().stringPretty(0)})")
    is Cat -> plain("Cat(${list0(this.xs.map { it.codify() }).stringPretty(0)})")
    is Nest -> plain("Nest(${this.i}, ${this.x.codify().stringPretty(0)})")
    is Union -> plain("Union(${this.long.codify().stringPretty(0)}, ${this.short.codify().stringPretty(0)})")
  }

  object Fail : Code()
  object Empty : Code()
  object Line : Code()
  data class CPlain(val c: Char) : Code()
  data class SPlain(val s: String) : Code()
  data class FlatAlt(val default: Code, val alt: Code) : Code()
  data class Cat(val xs: List<Code>) : Code()
  data class Nest(val i: Int, val x: Code) : Code()
  data class Union(val long: Code, val short: Code) : Code()
}
