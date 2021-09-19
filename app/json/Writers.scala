package json

import exception.NotSupportedCurrencyException
import play.api.libs.json.{JsObject, Json, Writes}

object Writers {
  implicit val NotSupportedCurrencyExceptionWrites: Writes[NotSupportedCurrencyException] = new Writes[NotSupportedCurrencyException] {
    def writes(exception: NotSupportedCurrencyException): JsObject = Json.obj(
      "error" -> exception.getMessage,
    )
  }
}
