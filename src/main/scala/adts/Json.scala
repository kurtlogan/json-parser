package adts

sealed trait Json
final case class JsonObject(tokens: JsonProperty*)          extends Json with JsValue
final case class JsonProperty(name: String, value: JsValue) extends Json

sealed trait JsValue
final case class JsString(value: String)        extends JsValue
final case class JsBoolean(value: Boolean)      extends JsValue
final case class JsNumber[T: Numeric](value: T) extends JsValue
final case class JsArray(values: JsValue*)      extends JsValue
final case object JsNull                        extends JsValue
