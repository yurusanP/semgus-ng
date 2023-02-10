package org.semgusng.pretty.base

internal sealed class SimpleCode {
  data class Line(val i: Int, val xs: List<SimpleCode>) : SimpleCode()
  data class CPlain(val c: Char) : SimpleCode()
  data class SPlain(val s: String) : SimpleCode()
}

internal fun Code.best(w: Int): List<SimpleCode> {
  fun fits(k: Int, ss: List<SimpleCode>): Boolean {
    var mutW = w - k
    if (mutW < 0) return false

    ss.forEach { s ->
      mutW -= when (s) {
        is SimpleCode.Line -> return true  // greedy
        is SimpleCode.CPlain -> 1
        is SimpleCode.SPlain -> s.s.length
      }
      if (mutW < 0) return false
    }

    return true
  }

  fun better(k: Int, ss: List<SimpleCode>, ss2: List<SimpleCode>) =
    if (fits(k, ss)) ss
    else ss2

  val be = DeepRecursiveFunction<Params, List<SimpleCode>> {

    (k, nests) ->

    if (nests.isEmpty()) return@DeepRecursiveFunction emptyList()

    val (i, x) = nests.removeLast()
    return@DeepRecursiveFunction when (x) {
      Code.Fail -> error("Unexpected Code.Fail.")
      Code.Empty -> callRecursive(k to nests)
      Code.Line -> listOf(SimpleCode.Line(i, callRecursive(i to nests)))
      is Code.CPlain -> listOf(SimpleCode.CPlain(x.c)) + callRecursive(k + 1 to nests)
      is Code.SPlain -> listOf(SimpleCode.SPlain(x.s)) + callRecursive(k + x.s.length to nests)
      is Code.FlatAlt -> let {
        nests += Code.Nest(i, x.default)
        callRecursive(k to nests)
      }

      is Code.Cat -> let {
        nests += x.xs.map { Code.Nest(i, it) }.asReversed()
        callRecursive(k to nests)
      }

      is Code.Nest -> let {
        nests += Code.Nest(i + x.i, x.x)
        callRecursive(k to nests)
      }

      is Code.Union -> let {
        val nests2 = mutableListOf<Code.Nest>().apply { addAll(nests) }
        nests += Code.Nest(i, x.long)
        nests2 += Code.Nest(i, x.short)
        better(k, callRecursive(k to nests), callRecursive(k to nests2))
      }
    }
  }

  return be(0 to mutableListOf(Code.Nest(0, this)))
}

typealias Params = Pair<Int, MutableList<Code.Nest>>
