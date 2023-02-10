package org.semgusng.pretty.render

import io.kotest.core.spec.style.FunSpec
import org.semgusng.pretty.base.*

class StringPrettyTest : FunSpec(
  {
    context("testHelloWorld") {
      val code = sep(plain("Hello"), plain("World"))
      println(code.stringPretty(120))
      println(code.stringPretty(10))
    }

    context("testList") {
      val code = list(listOf(
        hcat(plain("Hello"), line0()),
        plain("World"),
      ))
      println(code.stringPretty(120))
      println(code.stringPretty(10))
      println(code.codify().stringPretty(0))
    }
  },
)
