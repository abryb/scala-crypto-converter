package controllers.api

import controllers.api.currency.GetCurrencyRatesController
import exception.NotSupportedCurrencyException
import models.CurrencyRates
import org.scalatestplus.play._
import org.scalatestplus.mockito._
import org.mockito.Mockito._
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, Ok}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers.{GET, stubControllerComponents}
import services.currency.rates.RatesProvider

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Success, Try}

class GetCurrencyRatesControllerSpec extends PlaySpec with MockitoSugar {

  import scala.concurrent.ExecutionContext.Implicits.global

  "GetCurrencyRatesController" should {

    def await(future: Future[Result]): Try[Result] = {
      Await.ready(future, Duration.Inf).value.get
    }

    "return 200 OK (happy path!)" in {

      val ratesProviderMock = mock[RatesProvider]
      val rates = Map("BTC" -> 1.0, "ETH" -> 10.0)
      when(ratesProviderMock.getRates("BTC", Nil)).thenReturn(Future {
        CurrencyRates("BTC", rates)
      })

      val controller = new GetCurrencyRatesController(stubControllerComponents(), ratesProviderMock)

      val fakeRequest = FakeRequest(GET, "", FakeHeaders(), AnyContentAsEmpty)

      val result = await(controller.apply("BTC", Nil)(fakeRequest))

      result mustBe Success(Ok(Json.obj(
        "source" -> "BTC",
        "rates" -> Json.obj(
          "BTC" -> 1.0,
          "ETH" -> 10.0
        )
      ))
      )
    }

    "returns 400 currency is not supported if not supported currency" in {

      val ratesProviderMock = mock[RatesProvider]
      when(ratesProviderMock.getRates("fake", Nil)).thenReturn( Future {
        throw new NotSupportedCurrencyException("Currency fake is not supported!")
      })

      val controller = new GetCurrencyRatesController(stubControllerComponents(), ratesProviderMock)

      val fakeRequest = FakeRequest(GET, "", FakeHeaders(), AnyContentAsEmpty)

      val result = await(controller.apply("fake", Nil)(fakeRequest))

      result mustBe Success(BadRequest(Json.obj("error" -> "Currency fake is not supported!")))
    }

  }
}
