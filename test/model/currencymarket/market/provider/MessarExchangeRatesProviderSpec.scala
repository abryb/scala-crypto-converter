package model.currencymarket.market.provider

import model.currencymarket.market.provider.MessariExchangeRatesProvider
import model.currencymarket.market.provider.messari.MessariClient
import model.currencymarket.{Currency, CurrencyPair, ExchangeRate}
import org.scalatestplus.play._
import org.scalatestplus.mockito._
import org.mockito.Mockito._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Success, Try}

class MessarExchangeRatesProviderSpec extends PlaySpec with MockitoSugar {
  import scala.concurrent.ExecutionContext.Implicits.global
    "MessariQuotesProvider" should {
      // some helpers

      val USD = Currency("USD")
      val BTC = Currency("BTC")
      val ETH = Currency("ETH")

      def getMessariClientMock = {
        val mockMessariClient = mock[MessariClient]
        val ratesInUSD = Map("BTC" -> BigDecimal(40_000.0), "ETH" -> BigDecimal(4000.0))
        when(mockMessariClient.getRatesInUSD).thenReturn(Future {
          ratesInUSD
        })
        mockMessariClient
      }

      def await[T](future: Future[T]): Try[T] = {
        Await.ready(future, Duration.Inf).value.get
      }

      "provide set of exchange rates" in {
        val provider = new MessariExchangeRatesProvider(getMessariClientMock)
        await(provider.getExchangeRates) mustBe a[Success[Set[ExchangeRate]]]
      }

      "provides  right exchangeRates" in {
        val provider = new MessariExchangeRatesProvider(getMessariClientMock)

        val expected = Set(
          ExchangeRate(CurrencyPair(BTC, USD), 40_000.0),
          ExchangeRate(CurrencyPair(ETH, USD), 4_000.0),
        )

        await(provider.getExchangeRates) mustBe Success(expected)
      }
    }
}