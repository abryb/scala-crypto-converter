package view.json.form

import play.api.data.Form
import play.api.libs.json.{JsValue, Json}

class FormErrorsView[T](form: Form[T]) {
  def toJson : JsValue = {
    Json.parse("{}")
  }
}
