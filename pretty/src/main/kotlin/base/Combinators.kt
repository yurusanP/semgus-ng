package org.semgusng.pretty.base

import org.semgusng.pretty.base.Code.*

/**
 * The empty code, conceptually the monoid unit.
 */
fun empty() = Empty

/**
 * The hard line break '\n'.
 */
fun hardline() = Line

/**
 * Behaves like [Code.Empty] if the line break is undone by [group].
 */
fun line0() = flatAlt(hardline(), empty())

/**
 * Behaves like [space] if the line break is undone by [group].
 */
fun line() = flatAlt(hardline(), space())

fun softline0() = Union(empty(), hardline())

fun softline() = Union(space(), hardline())

/**
 * The plain character.
 *
 * @param c A character that is not '\n'
 */
fun plain(c: Char) = CPlain(c)

/**
 * The plain string.
 *
 * @param s A string without '\n'
 */
fun plain(s: String) = SPlain(s)

/**
 * Increases the nesting level of [x] by [i].
 */
fun nest(i: Int, x: Code) =
  if (i == 0) x
  else Nest(i, x)

/**
 * By default, [l] is rendered.
 * However, when [group]ed, [r] is preferred unless it doesn't fit.
 */
fun flatAlt(l: Code, r: Code) = FlatAlt(l, r)

/**
 * Tries to render [x] in a single line by removing the contained line breaks.
 * [x] won't change if it has a hard line break or doesn't fit.
 */
fun group(x: Code) = when (x) {
  is Union -> x
  is FlatAlt -> when (val f = flatCode(x.alt)) {
    FlatCode.Never -> x.default
    FlatCode.Already -> Union(x.alt, x.default)
    is FlatCode.Flattened -> Union(f.x, x.default)
  }
  else -> when (val f = flatCode(x)) {
    FlatCode.Never -> x
    FlatCode.Already -> x
    is FlatCode.Flattened -> Union(f.x, x)
  }
}

fun hcatDelim(d: Code, xs: List<Code>) = hcat(
  xs.flatMap { sequenceOf(d, it) }.drop(1).toList(),
)

fun hsepDelim(d: Code, xs: List<Code>) = hsep(
  xs.flatMap { sequenceOf(d, it) }.drop(1).toList(),
)

fun enclose(l: Code, r: Code, x: Code) = hcat(l, x, r)

fun hcatEncloseDelim(l: Code, r: Code, d: Code, xs: List<Code>) =
  enclose(l, r, hcatDelim(d, xs))

fun hsepEncloseDelim(l: Code, r: Code, d: Code, xs: List<Code>) =
  enclose(l, r, hsepDelim(d, xs))

/**
 * Concatenates [xs] horizontally with nothing.
 */
fun hcat(xs: List<Code>) = Cat(xs)

/**
 * @see [hcat]
 */
fun hcat(vararg xs: Code) = hcat(xs.toList())

/**
 * Concatenates [xs] vertically with [line0].
 */
fun vcat(xs: List<Code>) = hcatDelim(line0(), xs)

/**
 * @see [vcat]
 */
fun vcat(vararg xs: Code) = vcat(xs.toList())

/**
 * Grouped version of [vcat].
 */
fun cat(xs: List<Code>) = group(vcat(xs))

/**
 * @see [cat]
 */
fun cat(vararg xs: Code) = cat(xs.toList())

/**
 * Separates [xs] horizontally with [space].
 */
fun hsep(xs: List<Code>) = hcatDelim(space(), xs)

/**
 * @see [hsep]
 */
fun hsep(vararg xs: Code) = hsep(xs.toList())

/**
 * Separates [xs] vertically with [line].
 */
fun vsep(xs: List<Code>) = hcatDelim(line(), xs)

/**
 * @see [vsep]
 */
fun vsep(vararg xs: Code) = vsep(xs.toList())

/**
 * Grouped version of [vsep].
 */
fun sep(xs: List<Code>) = group(vsep(xs))

/**
 * @see [sep]
 */
fun sep(vararg xs: Code) = sep(xs.toList())

fun space() = plain(' ')
fun spaces(n: Int) =
  if (n <= 0) empty()
  else if (n == 1) space()
  else plain(" ".repeat(n))

fun squotes(x: Code) = enclose(plain('\''), plain('\''), x)
fun dquotes(x: Code) = enclose(plain('\"'), plain('\"'), x)
fun parens(x: Code) = enclose(plain('('), plain(')'), x)
fun angles(x: Code) = enclose(plain('<'), plain('>'), x)
fun brackets(x: Code) = enclose(plain('['), plain(']'), x)
fun braces(x: Code) = enclose(plain('{'), plain('}'), x)
fun verts(x: Code) = enclose(plain('|'), plain('|'), x)
fun semi(x: Code) = hcat(x, plain(';'))

fun set(xs: List<Code>) = group(
  hcatEncloseDelim(
    flatAlt(plain("{ "), plain('{')),
    flatAlt(plain(" }"), plain('}')),
    plain(", "),
    xs,
  ),
)

fun set0(xs: List<Code>) = hcatEncloseDelim(
  plain('{'),
  plain('}'),
  plain(", "),
  xs,
)

fun list(xs: List<Code>) = group(
  hcatEncloseDelim(
    flatAlt(plain("[ "), plain('[')),
    flatAlt(plain(" ]"), plain(']')),
    plain(", "),
    xs,
  ),
)

fun list0(xs: List<Code>) = hcatEncloseDelim(
  plain('['),
  plain(']'),
  plain(", "),
  xs,
)

fun tuple(xs: List<Code>) = group(
  hcatEncloseDelim(
    flatAlt(plain("( "), plain('(')),
    flatAlt(plain(" )"), plain(')')),
    plain(", "),
    xs,
  ),
)

fun tuple0(xs: List<Code>) = hcatEncloseDelim(
  plain('('),
  plain(')'),
  plain(", "),
  xs,
)

fun block(i: Int, prefix: Code, body: List<Code>) =
  if (body.none()) hsep(prefix, plain("{}"))
  else hsep(
    prefix,
    hcat(
      plain('{'),
      nest(i, hcat(line(), vsep(body))), line(),
      plain('}'),
    ),
  )
