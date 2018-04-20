package generators

import adts._
import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

object Json {
  private def numGen[T](implicit n: Numeric[T], c: Choose[T]) =
    Gen.oneOf(Gen.posNum[T], Gen.negNum[T])

  def arrayGen(maxDepth: Int): Gen[JsArray] =
    Gen.listOf(maxDepthValueGen(maxDepth)).map(xs ⇒ JsArray(xs: _*))

  val booleanGen: Gen[JsBoolean]     = Gen.oneOf(true, false).map(JsBoolean)
  val floatGen: Gen[JsNumber[Float]] = numGen[Float].map(JsNumber[Float])
  val intGen: Gen[JsNumber[Int]]     = numGen[Int].map(JsNumber[Int])
  val stringGen: Gen[JsString]       = Gen.alphaNumStr.map(JsString)

  def maxDepthValueGen(maxDepth: Int): Gen[JsValue] = maxDepth match {
    case i if i <= 0 ⇒
      Gen.oneOf(
        booleanGen,
        floatGen,
        intGen,
        stringGen,
        Gen.const(JsNull)
      )

    case i ⇒
      Gen.oneOf(
        arrayGen(i - 1),
        objectGen(i - 1),
        booleanGen,
        floatGen,
        intGen,
        stringGen,
        Gen.const(JsNull)
      )
  }

  def propertyGen(maxValueDepth: Int): Gen[JsonProperty] =
    for {
      name  ← Gen.alphaNumStr
      value ← maxDepthValueGen(maxValueDepth)
    } yield {
      JsonProperty(name, value)
    }

  def objectGen(maxDepth: Int): Gen[JsonObject] =
    Gen.listOf(propertyGen(maxDepth)).map(xs ⇒ JsonObject(xs: _*))

  val objectGen: Gen[JsonObject] = objectGen(3)
}
