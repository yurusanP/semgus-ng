package org.semgusng.pretty.base

internal sealed class FlatCode {
  object Never : FlatCode()
  object Already : FlatCode()
  data class Flattened(val x: Code) : FlatCode()
}

internal fun flatCode(x: Code): FlatCode = when (x) {
  Code.Fail -> FlatCode.Never
  Code.Line -> FlatCode.Never

  Code.Empty -> FlatCode.Already
  is Code.CPlain -> FlatCode.Already
  is Code.SPlain -> FlatCode.Already

  is Code.FlatAlt -> FlatCode.Flattened(flatten(x.alt))
  is Code.Cat -> flatHcat(x.xs.asSequence(), x.xs.asSequence().map(::flatCode))
  is Code.Nest -> flatNest(x.i, flatCode(x.x))
  is Code.Union -> FlatCode.Flattened(x.long)  // union could only be constructed by group
}

/**
 * Flattens without reporting.
 */
private fun flatten(x: Code): Code = when (x) {
  Code.Fail -> Code.Fail
  Code.Line -> Code.Fail

  Code.Empty -> x
  is Code.CPlain -> x
  is Code.SPlain -> x

  is Code.FlatAlt -> flatten(x.alt)
  is Code.Cat -> hcat(x.xs.map { flatten(it) })
  is Code.Nest -> nest(x.i, flatten(x.x))
  is Code.Union -> flatten(x.long)
}

private fun flatHcat(xs: Sequence<Code>, fs: Sequence<FlatCode>) = when {
  fs.any { it == FlatCode.Never } -> FlatCode.Never
  fs.all { it == FlatCode.Already } -> FlatCode.Already
  else -> FlatCode.Flattened(
    hcat(
      xs.zip(fs).map { (x, f) ->
        when (f) {
          FlatCode.Never -> error("Unexpected FlatCode.Never.")
          FlatCode.Already -> x
          is FlatCode.Flattened -> f.x
        }
      }.toList(),
    ),
  )
}

private fun flatNest(i: Int, f: FlatCode) = when (f) {
  FlatCode.Never -> FlatCode.Never
  FlatCode.Already -> FlatCode.Already
  is FlatCode.Flattened -> FlatCode.Flattened(nest(i, f.x))
}
