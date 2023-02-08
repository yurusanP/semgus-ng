package org.semgusng.parser.serialization

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import org.semgusng.parser.event.ParseEventSerializer

class SerializationUtilsTest : FunSpec(
  {
    context("Semgus-Benchmarks/integer-arithmetics/max2-exp") {
      withData(jsonList) { json ->
        println(json)
        val event = json.decode(ParseEventSerializer)
        println(event)
        val json2 = event.encode(ParseEventSerializer)
        println(json2)
        json2 shouldEqualJson json
      }
    }

    xcontext("dust-json/max2-exp") {
      javaClass.getResource("/dust-json/max2-exp.json")?.readText()?.let { json ->
        val events = json.decodeBatch(ParseEventSerializer)
        println(events)
        val json2 = events.encodeBatch(ParseEventSerializer)
        println(json2)
        json2 shouldEqualJson json
      }
    }
  },
)
