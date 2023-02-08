package org.semgusng.parser.serialization

val jsonList = listOf(
  """
    {
      "keyword": "format-version",
      "value": "2.1.0",
      "${'$'}event": "set-info",
      "${'$'}type": "meta"
    }
  """.trimIndent(),
  """
    {
      "keyword": "author",
      "value": [
        "Jinwoo Kim",
        "Keith Johnson",
        "Wiley Corning"
      ],
      "${'$'}event": "set-info",
      "${'$'}type": "meta"
    }
  """.trimIndent(),
  """
    {
      "keyword": "realizable",
      "value": "true",
      "${'$'}event": "set-info",
      "${'$'}type": "meta"
    }
  """.trimIndent(),
  """
    {
      "name": "E",
      "${'$'}event": "declare-term-type",
      "${'$'}type": "semgus"
    }
  """.trimIndent(),
  """
    {
      "name": "B",
      "${'$'}event": "declare-term-type",
      "${'$'}type": "semgus"
    }
  """.trimIndent(),
  """
    {
      "name": "E",
      "constructors": [
        {
          "name": "${'$'}x",
          "children": []
        },
        {
          "name": "${'$'}y",
          "children": []
        },
        {
          "name": "${'$'}0",
          "children": []
        },
        {
          "name": "${'$'}1",
          "children": []
        },
        {
          "name": "${'$'}+",
          "children": [
            "E",
            "E"
          ]
        },
        {
          "name": "${'$'}ite",
          "children": [
            "B",
            "E",
            "E"
          ]
        }
      ],
      "${'$'}event": "define-term-type",
      "${'$'}type": "semgus"
    }
  """.trimIndent(),
  """
    {
      "name": "B",
      "constructors": [
        {
          "name": "${'$'}t",
          "children": []
        },
        {
          "name": "${'$'}f",
          "children": []
        },
        {
          "name": "${'$'}not",
          "children": [
            "B"
          ]
        },
        {
          "name": "${'$'}and",
          "children": [
            "B",
            "B"
          ]
        },
        {
          "name": "${'$'}or",
          "children": [
            "B",
            "B"
          ]
        },
        {
          "name": "${'$'}<",
          "children": [
            "E",
            "E"
          ]
        }
      ],
      "${'$'}event": "define-term-type",
      "${'$'}type": "semgus"
    }
  """.trimIndent(),
  """
    {
      "name": "E.Sem",
      "rank": {
        "argumentSorts": [
          "E",
          "Int",
          "Int",
          "Int"
        ],
        "returnSort": "Bool"
      },
      "${'$'}event": "declare-function",
      "${'$'}type": "smt"
    }
  """.trimIndent(),
  """
    {
      "name": "B.Sem",
      "rank": {
        "argumentSorts": [
          "B",
          "Int",
          "Int",
          "Bool"
        ],
        "returnSort": "Bool"
      },
      "${'$'}event": "declare-function",
      "${'$'}type": "smt"
    }
  """.trimIndent(),
)
