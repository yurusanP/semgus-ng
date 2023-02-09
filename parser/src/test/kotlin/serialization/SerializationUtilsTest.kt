package org.semgusng.parser.serialization

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonObject
import org.semgusng.parser.event.ParseEvent

class SerializationUtilsTest : FunSpec(
  {
    context("Basic serialization") {
      withData(basicJsonList) { json ->
        println(json)
        val event = json.decode(ParseEvent.serializer())
        println(event)
        val json2 = event.encode(ParseEvent.serializer())
        println(json2)
        json2 shouldEqualJson json
      }
    }

    context("integer-arithmetic/max2-exp") {
      javaClass.getResource("/parser/serialization/max2-exp.json")?.readText()?.let { jsonArr ->
        val jsonList = jsonArr.decode(ListSerializer(JsonObject.serializer())).map { it.toString() }
        withData(jsonList) { json ->
          println(json)
          val event = json.decode(ParseEvent.serializer())
          println(event)
          val json2 = event.encode(ParseEvent.serializer())
          println(json2)
          json2 shouldEqualJson json
        }
      }
    }
  },
)
