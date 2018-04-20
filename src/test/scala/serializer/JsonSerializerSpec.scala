package serializer

import utils.UnitSpec
import generators.Json.objectGen
import parser.JsonParser.parse
import serializer.JsonSerializer.serialize

class JsonSerializerSpec extends UnitSpec {

  import utils.Pipe.implicits

  "serializer" should {
    "serialize and parse a JsonObject" in {
      forAll(objectGen) { obj ⇒
        serialize(obj) |> parse shouldBe Right(obj)
      }
    }
  }
}
