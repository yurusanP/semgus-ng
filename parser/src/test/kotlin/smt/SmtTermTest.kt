package org.semgusng.parser.smt

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import org.semgusng.parser.serialization.decode
import org.semgusng.parser.serialization.encode

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
        val term = json.decode(SmtTerm.serializer())
        println(term)
        val json2 = term.encode(SmtTerm.serializer())
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

    context("testSmtMatchBinder") {
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
      val term = json.decode(SmtTerm.serializer())
      println(term)
      val json2 = term.encode(SmtTerm.serializer())
      println(json2)
      json2 shouldEqualJson json
    }

    context("testAnnotation") {
      val json = """
        {
          "term": {
            "name": "et",
            "sort": "E",
            "${'$'}termType": "variable"
          },
          "binders": [],
          "${'$'}termType": "match",
          "annotations": [
            {
              "keyword": {
                "name": "input"
              },
              "value": [
                "x",
                "y"
              ]
            },
            {
              "keyword": {
                "name": "output"
              },
              "value": [
                "r"
              ]
            }
          ]
        }
      """.trimIndent()
      println(json)
      val term = json.decode(SmtTerm.serializer())
      println(term)
      val json2 = term.encode(SmtTerm.serializer())
      println(json2)
      json2 shouldEqualJson json
    }

    context("testSmtMatchGrouper") {
      val json = """
        {
          "term": {
            "name": "et",
            "sort": "E",
            "${'$'}termType": "variable"
          },
          "binders": [],
          "${'$'}termType": "match"
        }
      """.trimIndent()
      println(json)
      val term = json.decode(SmtTerm.serializer())
      println(term)
      val json2 = term.encode(SmtTerm.serializer())
      println(json2)
      json2 shouldEqualJson json
    }
  },
)
