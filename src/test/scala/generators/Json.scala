package generators

import adts._
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

object Json {
  private def numGen[T](implicit n: Numeric[T], c: Choose[T]) =
    Gen.oneOf(Gen.posNum[T], Gen.negNum[T])

  lazy val arrayGen = Gen.listOf(valueGen).map(xs ⇒ JsArray(xs: _*))
  val booleanGen    = Gen.oneOf(true, false).map(JsBoolean)
  val floatGen      = numGen[Float].map(JsNumber[Float])
  val intGen        = numGen[Int].map(JsNumber[Int])
  val stringGen     = Gen.alphaNumStr.map(JsString)

  lazy val valueGen: Gen[JsValue] = Gen.oneOf(
    arrayGen,
    booleanGen,
    floatGen,
    intGen,
    objectGen,
    stringGen,
    Gen.const(JsNull)
  )

  val propertyGen: Gen[JsonProperty] = for {
    name  ← Gen.alphaNumStr
    value ← valueGen
  } yield {
    JsonProperty(name, value)
  }

  val objectGen: Gen[JsonObject] =
    Gen.listOf(propertyGen).map(xs ⇒ JsonObject(xs: _*))
}
