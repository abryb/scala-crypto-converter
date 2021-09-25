package model.currencymarket.market

import model.currencymarket.exception.NotSupportedCurrencyPairException
import model.currencymarket.{Currency, CurrencyPair, ExchangeRate}
import org.scalatestplus.play._

class SimpleMarketSpec extends PlaySpec {
  "A SimpleMarket" must {
    val USD = Currency("USD")
    val EUR = Currency("EUR")
    val PLN = Currency("PLN")

    val EUR_USD = CurrencyPair(EUR, USD)
    val EUR_PLN = CurrencyPair(EUR, PLN)
    val USD_PLN = CurrencyPair(USD, PLN)

    val EUR_USD_rate = ExchangeRate(EUR_USD, 1.2)
    val EUR_PLN_rate = ExchangeRate(EUR_PLN, 4.4)
    val market =  new SimpleMarket {
      override def getSupportedCurrencies: Set[Currency] = Set(EUR, USD, PLN)

      override def getExchangeRate(currencyPair: CurrencyPair): Option[ExchangeRate] = currencyPair match {
        case EUR_USD => Some(EUR_USD_rate)
        case EUR_PLN => Some(EUR_PLN_rate)
        case _ => None
      }
    }

    "hasExchangeRateFor" must {
      "be valid" in {
        market.hasExchangeRateFor(EUR_USD) mustBe true
        market.hasExchangeRateFor(USD_PLN) mustBe false
      }
    }

    "getExchangeRatesForBaseCurrency" must {
      "return all exchange rates if None quoteCurrencies given" in {
        market.getExchangeRatesForBaseCurrency(EUR, None).toSet mustBe Set(EUR_USD_rate, EUR_PLN_rate)
      }

      "return only given quoteCurrencies exchange rates" in {
        market.getExchangeRatesForBaseCurrency(EUR, Some(Seq(USD))) mustBe Seq(EUR_USD_rate)
      }

      "throw NotSupportedCurrencyPairException if can't get rate for given quoteCurrency" in {
        assertThrows[NotSupportedCurrencyPairException] {
          market.getExchangeRatesForBaseCurrency(PLN, Some(Seq(EUR)))
        }
      }
    }
  }
}
