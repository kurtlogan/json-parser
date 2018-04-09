package adts

sealed trait Json
case class JsonObject(tokens: Json*)                  extends Json with JsValue
case class JsonProperty(name: String, value: JsValue) extends Json

sealed trait JsValue
case class JsString(value: String)        extends JsValue
case class JsBoolean(value: Boolean)      extends JsValue
case class JsNumber[T: Numeric](value: T) extends JsValue
case class JsArray(values: JsValue*)      extends JsValue
