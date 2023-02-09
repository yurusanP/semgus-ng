package org.semgusng.parser.model.smt

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import org.semgusng.parser.serialization.decode
import org.semgusng.parser.serialization.encode
import org.semgusng.parser.smt.SmtTermSerializer

class SmtTermTest : FunSpec(
  {
    context("testSmtLiteral") {
      withData(
        listOf(
          "1",
          "1.0",
          "\"et\"",
          "-23",
        ),
      ) { json ->
        println(json)
        val term = json.decode(SmtTermSerializer)
        println(term)
        val json2 = term.encode(SmtTermSerializer)
        println(json2)
        json2 shouldEqualJson json
      }
    }

    context("testSmtLambdaBinder") {
      val json = """
        {
          "arguments": [
            "et",
            "x",
            "y",
            "r"
          ],
          "body": {
            "name": "r",
            "sort": "Int",
            "${'$'}termType": "variable"
          },
          "${'$'}termType": "lambda"
        }
      """.trimIndent()
      println(json)
      val term = json.decode(SmtTermSerializer)
      println(term)
      val json2 = term.encode(SmtTermSerializer)
      println(json2)
      json2 shouldEqualJson json
    }

    context("testSmtBinder") {
      val json = """
        {
          "operator": "${'$'}t",
          "arguments": [],
          "child": {
            "name": "=",
            "returnSort": "Bool",
            "argumentSorts": [
              "Bool",
              "Bool"
            ],
            "arguments": [
              {
                "name": "r",
                "sort": "Bool",
                "${'$'}termType": "variable"
              },
              {
                "name": "true",
                "returnSort": "Bool",
                "argumentSorts": [],
                "arguments": [],
                "${'$'}termType": "application"
              }
            ],
            "${'$'}termType": "application"
          },
          "${'$'}termType": "binder"
        }
      """.trimIndent()
      println(json)
      val term = json.decode(SmtTermSerializer)
      println(term)
      val json2 = term.encode(SmtTermSerializer)
      println(json2)
      json2 shouldEqualJson json
    }
  },
)
