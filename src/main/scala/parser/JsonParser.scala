package parser

import adts._

object JsonParser extends StringParser {

  lazy val obj
    : Parser[JsonObject] = leftBrace ~> repsep(property, comma) <~ rightBrace <~ (semiColon ?) ^^ {
    JsonObject(_: _*)
  }

  lazy val property: Parser[Json] = name ~ value ^^ {
    case n ~ v ⇒ JsonProperty(n, v)
  }

  lazy val name: Parser[String] = enclosedString <~ colon ^^ id

  lazy val value
    : JsonParser.Parser[JsValue] = (enclosedString | boolean | numberFloat | numberInt | array | obj) ^^ {
    case s: String  ⇒ JsString(s)
    case b: Boolean ⇒ JsBoolean(b)
    case f: Float   ⇒ JsNumber(f)
    case i: Int     ⇒ JsNumber(i)

    // Type is unchecked and eliminated by erasure so all lists will match - need to fix
    case a: List[JsValue] ⇒ JsArray(a: _*)
    case o: JsonObject    ⇒ o
  }

  lazy val array
    : Parser[List[JsValue]] = leftSquare ~> repsep(value, comma) <~ rightSquare ^^ id

  lazy val json: Parser[JsonObject] = phrase(obj)

  def parse(input: String): Either[String, JsonObject] =
    super.parse(json, input) match {
      case Success(result, _) ⇒ Right(result)
      case NoSuccess(_, _)    ⇒ Left("failed to parse json")
    }

  private def id[A](x: A) = x
}
