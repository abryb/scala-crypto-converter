package controllers.api.currency

import models.CurrencyRates
import exception.NotSupportedCurrencyException
import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import services.currency.rates.RatesProvider

import scala.util.{Failure, Success}


class GetCurrencyRatesController @Inject()(
                                            cc: ControllerComponents,
                                            rateProvider: RatesProvider
                                          ) extends AbstractController(cc) {

  implicit val resultWrites: Writes[CurrencyRates] = new Writes[CurrencyRates] {
    def writes(rates: CurrencyRates): JsValue = JsObject(Map(
      "source" -> JsString(rates.baseCurrencyCode),
      "rates" -> JsObject(rates.rates.view.mapValues(JsNumber(_)).toMap)
    ))
  }

  implicit val exceptionWrites: Writes[NotSupportedCurrencyException] = new Writes[NotSupportedCurrencyException] {
    def writes(exception: NotSupportedCurrencyException): JsValue = JsObject(Map(
      "error" -> JsString(exception.getMessage),
    ))
  }

  def apply(code: String, filter: List[String]): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    rateProvider.getRates(code, filter).transform {
      case Success(value) => Success(Ok(Json.toJson(value)))
      case Failure(exception: NotSupportedCurrencyException) => Success(BadRequest(Json.toJson(exception)))
      case Failure(exception) => Failure(exception)
    }
  }
}
