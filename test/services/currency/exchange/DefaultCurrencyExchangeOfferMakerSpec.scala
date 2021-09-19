package services.currency.exchange

import models.{CurrencyExchangeOffer, CurrencyExchangeOfferOption, CurrencyRates}
import org.scalatestplus.play._
import org.scalatestplus.mockito._
import org.mockito.Mockito._
import services.currency.rates.RatesProvider

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Success, Try}

class DefaultCurrencyExchangeOfferMakerSpec extends PlaySpec with MockitoSugar {
  import scala.concurrent.ExecutionContext.Implicits.global

  "DefaultCurrencyExchangeOfferMaker" should {
    def getMockedMaker: DefaultCurrencyExchangeOfferMaker = {
      val ratesProviderMock = mock[RatesProvider]
      when(ratesProviderMock.getRates("BTC", List("ETH"))).thenReturn(Future{CurrencyRates("BTC", Map("ETH" -> 10))})
      new DefaultCurrencyExchangeOfferMaker(ratesProviderMock)
    }

    def await(future : Future[CurrencyExchangeOffer]) : Try[CurrencyExchangeOffer] = {
      Await.ready(future, Duration.Inf).value.get
    }

    "makes good offer" in {
      val result = await(getMockedMaker.getOffer("BTC", List("ETH"), 1))
      result mustBe Success(CurrencyExchangeOffer("BTC", Map("ETH" -> CurrencyExchangeOfferOption(rate = 10, amount = 1, result = 9.9 ,fee = 0.01))))
    }
  }
}