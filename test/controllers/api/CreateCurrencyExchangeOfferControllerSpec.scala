package controllers.api

import controllers.api.currency.CreateCurrencyExchangeOfferController
import exception.NotSupportedCurrencyException
import models.{CurrencyExchangeOffer, CurrencyExchangeOfferOption}
import org.scalatestplus.play._
import org.scalatestplus.mockito._
import org.mockito.Mockito._
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, Ok}
import play.api.mvc.{AnyContentAsJson, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers.{POST, stubControllerComponents}
import services.currency.exchange.CurrencyExchangeOfferMaker

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Success, Try}

class CreateCurrencyExchangeOfferControllerSpec extends PlaySpec with MockitoSugar {

  import scala.concurrent.ExecutionContext.Implicits.global

  "CreateCurrencyExchangeOfferController" should {

    def await(future: Future[Result]): Try[Result] = {
      Await.ready(future, Duration.Inf).value.get
    }

    "return 200 OK (happy path!)" in {
      val offerMakerMock = mock[CurrencyExchangeOfferMaker]
      when(offerMakerMock.getOffer("BTC", List("ETH"), 1)).thenReturn(Future {
        CurrencyExchangeOffer("BTC", Map("ETH" -> CurrencyExchangeOfferOption(rate = 10, amount = 1, result = 9.9, fee = 0.01)))
      })
      val controller = new CreateCurrencyExchangeOfferController(stubControllerComponents(), offerMakerMock)

      val requestBody = Json.obj(
        "from" -> "BTC",
        "to" -> Json.arr("ETH"),
        "amount" -> 1
      )
      val fakeRequest = FakeRequest(POST, "", FakeHeaders(), AnyContentAsJson(requestBody))

      val result = await(controller.apply()(fakeRequest))

      result mustBe Success(Ok(Json.obj(
        "from" -> "BTC",
        "ETH" -> Json.obj(
          "rate" -> 10.0,
          "amount" -> 1,
          "result" -> 9.9,
          "fee" -> 0.01
        )
      )))
    }

    "returns 400 currency is not supported if not supported currency" in {
      val offerMakerMock = mock[CurrencyExchangeOfferMaker]
      when(offerMakerMock.getOffer("notSupportedCurrency", List("ETH"), 1)).thenReturn(Future {
        throw new NotSupportedCurrencyException("Currency fake is not supported!")
      })
      val controller = new CreateCurrencyExchangeOfferController(stubControllerComponents(), offerMakerMock)

      val requestBody = Json.obj(
        "from" -> "notSupportedCurrency",
        "to" -> Json.arr("ETH"),
        "amount" -> 1
      )
      val fakeRequest = FakeRequest(POST, "", FakeHeaders(), AnyContentAsJson(requestBody))

      val result = await(controller.apply()(fakeRequest))

      result mustBe Success(BadRequest(Json.obj(
        "error" -> "Currency fake is not supported!",
      )))
    }

    "returns only options for supported currencies" in {
      val offerMakerMock = mock[CurrencyExchangeOfferMaker]
      when(offerMakerMock.getOffer("BTC", List("ETH", "notSupportedCurrency"), 1)).thenReturn(Future {
        CurrencyExchangeOffer("BTC", Map("ETH" -> CurrencyExchangeOfferOption(rate = 10, amount = 1, result = 9.9, fee = 0.01)))
      })
      val controller = new CreateCurrencyExchangeOfferController(stubControllerComponents(), offerMakerMock)

      val requestBody = Json.obj(
        "from" -> "BTC",
        "to" -> Json.arr("ETH", "notSupportedCurrency"),
        "amount" -> 1
      )
      val fakeRequest = FakeRequest(POST, "", FakeHeaders(), AnyContentAsJson(requestBody))

      val result = await(controller.apply()(fakeRequest))

      result mustBe Success(Ok(Json.obj(
        "from" -> "BTC",
        "ETH" -> Json.obj(
          "rate" -> 10.0,
          "amount" -> 1,
          "result" -> 9.9,
          "fee" -> 0.01
        )
      )))
    }
  }
}
