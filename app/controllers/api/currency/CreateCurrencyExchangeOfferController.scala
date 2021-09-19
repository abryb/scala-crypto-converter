package controllers.api.currency

import exception.NotSupportedCurrencyException
import models.CurrencyExchangeOffer
import json.Writers.NotSupportedCurrencyExceptionWrites

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.data.Form
import services.currency.exchange.CurrencyExchangeOfferMaker

import scala.util.{Failure, Success}

class CreateCurrencyExchangeOfferController @Inject()(
                                                       cc: ControllerComponents,
                                                       offerMaker: CurrencyExchangeOfferMaker
                                                     ) extends AbstractController(cc) {

  case class Input(from: String, to: List[String], amount: Double)

  private val InputForm: Form[Input] = {
    import play.api.data.Forms._
    import play.api.data.format.Formats._
    Form(mapping(
      "from" -> nonEmptyText,
      "to" -> list(text),
      "amount" -> of(doubleFormat)
    )(Input.apply)(Input.unapply)
    )
  }

  implicit val resultWrites: Writes[CurrencyExchangeOffer] = new Writes[CurrencyExchangeOffer] {
    def writes(result: CurrencyExchangeOffer): JsValue = Json.obj(
      "from" -> result.from
    ).deepMerge(
      JsObject(result.to.map {
        case (key, value) => (key, Json.obj(
          "rate" -> value.rate,
          "amount" -> value.amount,
          "result" -> value.result,
          "fee" -> value.fee
        ))
      }
      )
    )
  }

  implicit val errorFormWrites: Writes[Form[Input]] = new Writes[Form[Input]] {
    def writes(errorForm: Form[Input]): JsValue = JsObject(Map(
      "errors" -> JsObject(errorForm.errors.map(e => (e.key, JsString(e.message))).toMap)
    ))
  }

  def apply(): Action[AnyContent] =
    Action.async { implicit request: Request[AnyContent] =>
      InputForm.bindFromRequest.fold(
        // if any error in submitted data
        errorForm => {
          Future {
            UnprocessableEntity(Json.toJson(errorForm))
          }
        },
        data => {
          offerMaker.getOffer(data.from, data.to, data.amount).transform{
            case Success(value) => Success(Ok(Json.toJson(value)))
            case Failure(exception: NotSupportedCurrencyException) => Success(BadRequest(Json.toJson(exception)))
            case Failure(exception) => Failure(exception)
          }
        }
      )
    }
}
