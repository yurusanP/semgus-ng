package org.semgusng.parser.serialization

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.funSpec
import io.kotest.datatest.withData
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonObject
import org.semgusng.parser.event.ParseEvent

fun singleTest(path: String) = funSpec {
  context(path) {
    javaClass.getResource(path)?.readText()?.let { jsonArr ->
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
}

class SerializationUtilsTest : FunSpec(
  {
    include(singleTest("/parser/serialization/integer-arithmetic/max2-exp.json"))
    include(singleTest("/parser/serialization/integer-arithmetic/max3-exp.json"))
    include(singleTest("/parser/serialization/integer-arithmetic/plus-2-times-3.json"))
    include(singleTest("/parser/serialization/integer-arithmetic/polynomial.json"))

    include(singleTest("/parser/serialization/imperative/double-by-increment-loop.json"))
    include(singleTest("/parser/serialization/imperative/identity-by-increment-loop.json"))
    // Fixme: existence of null?
//    include(singleTest("/parser/serialization/imperative/mul-by-while.json"))
  },
)
