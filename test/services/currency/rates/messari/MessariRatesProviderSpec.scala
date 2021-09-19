package services.currency.rates.messari

import models.CurrencyRates
import org.scalatestplus.play._
import org.scalatestplus.mockito._
import org.mockito.Mockito._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Success, Try}

class MessariRatesProviderSpec extends PlaySpec with MockitoSugar {
  import scala.concurrent.ExecutionContext.Implicits.global

  "MessariRatesProvider" should {
    def getMockedRatesProvider: MessariRatesProvider = {
      val mockMessariClient = mock[MessariClientInterface]
      val ratesInUSD  = Map("BTC" -> 40_000.0, "ETH"-> 4000.0)
      when(mockMessariClient.getRatesInUSD).thenReturn(Future{ratesInUSD})
      new MessariRatesProvider(mockMessariClient)
    }

    def await(future : Future[CurrencyRates]) : Try[CurrencyRates] = {
      Await.ready(future, Duration.Inf).value.get
    }

    "calculate good rate" in {
      val result = await(getMockedRatesProvider.getRates("BTC", Nil))
      result mustBe Success(CurrencyRates("BTC", Map("BTC" -> 1, "ETH" -> 10)))
    }

    "get all rates if  Nil filter given " in {
      val result = await(getMockedRatesProvider.getRates("BTC", Nil))
      result match {
        case Success(CurrencyRates("BTC", rates)) => rates.keys.toSet mustBe Set("ETH", "BTC")
        case _ => false mustBe true
      }
    }

    "get rates from filter only if not Nil filter given" in {
      val result = await(getMockedRatesProvider.getRates("BTC", List("ETH")))
      result mustBe Success(CurrencyRates("BTC", Map("ETH" -> 10)))
    }

    "get rates with order from filter" in {
      val result = await(getMockedRatesProvider.getRates("BTC", List("ETH", "BTC")))
      result mustBe Success(CurrencyRates("BTC", Map("ETH" -> 10,  "BTC" -> 1)))

      val result2 = await(getMockedRatesProvider.getRates("BTC", List("BTC", "ETH")))
      result2 mustBe Success(CurrencyRates("BTC", Map("BTC" -> 1,  "ETH" -> 10)))
    }

    "get rates if non upper case input given" in {
      val result = await(getMockedRatesProvider.getRates("btc", List("eth")))
      result mustBe Success(CurrencyRates("BTC", Map("ETH" -> 10)))
    }
  }
}
