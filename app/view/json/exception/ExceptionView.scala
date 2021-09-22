package view.json.exception

import play.api.libs.json.{JsValue, Json}

class ExceptionView(exception: Exception) {
  def toJson : JsValue = {
     Json.parse("{}")
  }
}
