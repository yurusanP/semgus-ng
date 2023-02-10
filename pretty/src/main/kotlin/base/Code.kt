package org.semgusng.pretty.base

/**
 * [Code] should of course be pretty.
 */
sealed class Code {
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
