package parser

import adts._
import org.scalacheck.Gen
import utils.UnitSpec

class JsonSpec extends UnitSpec {
  private val nameValueGen = for {
    name  ← Gen.alphaNumStr
    value ← Gen.alphaNumStr
  } yield {
    (name, value)
  }

  private val nameValuesGen = Gen.listOf(nameValueGen)

  "parse" should {
    import JsonParser.parse

    "return failed to parse" when {
      val failedParse = "failed to parse json"

      """qwerty is passed""" in {
        parse("qwerty").merge shouldBe failedParse
      }

      """mixed " and ' are passed""" in {
        parse("""{"hello':'world"}""").merge shouldBe failedParse
      }

      "new line in name" in {
        parse("{'hel\nlo': 'world'}").merge shouldBe failedParse
      }

      "new line in value" in {
        parse("{'hello': 'wor\nld'}").merge shouldBe failedParse
      }

      "there is any trailing characters" in {
        parse("{'obj': {}}'").merge shouldBe failedParse
      }
    }

    "return Object" when {
      "an {} is passed" in {
        parse("{}").merge shouldBe JsonObject()
      }

      "there is a trailing ;" in {
        parse("{};").merge shouldBe JsonObject()
      }
    }

    "return an object with a string value" when {
      """{"hello":"world"} is passed""" in {
        forAll(nameValueGen) {
          case (name, value) ⇒
            parse(s"""{"$name":"$value"}""").merge shouldBe JsonObject(
              JsonProperty(name, JsString(value))
            )
        }
      }

      "{'hello':'world'} is passed" in {
        forAll(nameValueGen) {
          case (name, value) ⇒
            parse(s"{'$name':'$value'}").merge shouldBe JsonObject(
              JsonProperty(name, JsString(value))
            )
        }
      }

      // TODO: need to consider escaped " and ' characters in strings

      "{'hello': 'world', 'foo': 'bar'}" in {
        forAll(nameValuesGen) { nameValues ⇒
          val properties = nameValues
            .map { case (name, value) ⇒ s"'$name': '$value'" }
            .mkString(",\n")

          parse(s"{$properties}").merge shouldBe JsonObject(
            nameValues.map {
              case (name, value) ⇒ JsonProperty(name, JsString(value))
            }: _*
          )
        }
      }
    }

    "return an object with a boolean value" when {
      "{'isHello':true} is passed" in {
        parse("{'isHello':true}").merge shouldBe JsonObject(
          JsonProperty("isHello", JsBoolean(true))
        )
      }

      "{'isHello':false} is passed" in {
        parse("{'isHello':false}").merge shouldBe JsonObject(
          JsonProperty("isHello", JsBoolean(false))
        )
      }
    }

    "return an object with a number value" when {
      "{'howMuch': 1} is passed" in {
        forAll { i: Int ⇒
          parse(s"{'howMuch': $i}").merge shouldBe JsonObject(
            JsonProperty("howMuch", JsNumber(i))
          )
        }
      }

      "{'howMuch': 1.0} is passed" in {
        forAll { i: Float ⇒
          parse(s"{'howMuch': $i}").merge shouldBe JsonObject(
            JsonProperty("howMuch", JsNumber(i))
          )
        }
      }
    }

    "return an object with an array" when {
      "{'arr': []} is passed" in {
        parse("{'arr': []}").merge shouldBe JsonObject(
          JsonProperty("arr", JsArray())
        )
      }

      "{'arr': ['hello world']} is passed" in {
        forAll { value: String ⇒
          parse(s"{'arr': ['$value']}").merge shouldBe JsonObject(
            JsonProperty("arr", JsArray(JsString(value)))
          )
        }
      }

      "{'arr': [true]} is passed" in {
        parse("{'arr': [true]}").merge shouldBe JsonObject(
          JsonProperty("arr", JsArray(JsBoolean(true)))
        )
      }

      "{'arr': [false]} is passed" in {
        parse("{'arr': [false]}").merge shouldBe JsonObject(
          JsonProperty("arr", JsArray(JsBoolean(false)))
        )
      }

      "{'arr': [1]} is passed" in {
        forAll { value: Int ⇒
          parse(s"{'arr': [$value]}").merge shouldBe JsonObject(
            JsonProperty("arr", JsArray(JsNumber(value)))
          )
        }
      }

      "{'arr': [1.0]} is passed" in {
        forAll { value: Float ⇒
          parse(s"{'arr': [$value]}").merge shouldBe JsonObject(
            JsonProperty("arr", JsArray(JsNumber(value)))
          )
        }
      }

      "{'arr': [[]]} is passed" in {
        parse("{'arr': [[]]}").merge shouldBe JsonObject(
          JsonProperty("arr", JsArray(JsArray()))
        )
      }

      "{'arr': ['hello', 'world']} is passed" in {
        forAll(nameValuesGen) { nameValues ⇒
          val values =
            nameValues.map { case (_, value) ⇒ s"'$value'" }.mkString(",\n")

          parse(s"{'arr': [$values]}").merge shouldBe JsonObject(
            JsonProperty(
              "arr",
              JsArray(
                nameValues.map { case (_, value) ⇒ JsString(value) }: _*
              )
            )
          )
        }
      }
    }

    "return an object with an object" when {
      "{'obj': {}}" in {
        parse("{'obj': {}}").merge shouldBe JsonObject(
          JsonProperty("obj", JsonObject())
        )
      }
    }

    "complete test" in {
      val input =
        """
          |{
          | "obj": {
          |   'with':
          |           'value',
          |   "amount": 1000,
          |   "asFloat": 1000.99
          | },
          | 'list-of-values': [
          |   {},
          |   "foobar",
          |   88008,
          |   1234.1234
          | ]
          |}
          |""".stripMargin

      parse(input).merge shouldBe JsonObject(
        JsonProperty(
          "obj",
          JsonObject(
            JsonProperty("with", JsString("value")),
            JsonProperty("amount", JsNumber(1000)),
            JsonProperty("asFloat", JsNumber(1000.99f))
          )
        ),
        JsonProperty(
          "list-of-values",
          JsArray(
            JsonObject(),
            JsString("foobar"),
            JsNumber(88008),
            JsNumber(1234.1234f)
          )
        )
      )

    }
  }
}
