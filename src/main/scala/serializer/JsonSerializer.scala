package serializer

import adts._

object JsonSerializer {
  def serialize(obj: JsonObject): String = serializeValue(obj)

  private def serializeValue(value: JsValue): String = value match {
    case JsBoolean(true)     ⇒ "true"
    case JsBoolean(false)    ⇒ "false"
    case JsNumber(i)         ⇒ i.toString
    case JsString(s)         ⇒ s""""$s""""
    case JsNull              ⇒ "null"
    case JsArray(xs @ _*)    ⇒ s"[${xs.map(serializeValue).mkString(",")}]"
    case JsonObject(xs @ _*) ⇒ s"{${xs.map(serializeProperty).mkString(",")}}"
  }

  private def serializeProperty(prop: JsonProperty): String =
    s""""${prop.name}": ${serializeValue(prop.value)}"""
}
