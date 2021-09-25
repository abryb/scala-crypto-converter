package model.currencymarket.market

import model.currencymarket.{Currency, CurrencyPair, ExchangeRate}
import org.scalatestplus.play._

class BaseCurrencyMarketSpec extends PlaySpec {
  "A SingleCurrencyBasedMarket" must {
    val USD = Currency("USD")
    val EUR = Currency("EUR")
    val PLN = Currency("PLN")

    val quoteCurrencies = Map(
      USD -> BigDecimal(1.25),
      PLN -> BigDecimal(5.0)
    )

    val market =  BaseCurrencyMarket(EUR, quoteCurrencies)

    "getSupportedCurrencies" must {
      "return supported currencies" in {
        market.getSupportedCurrencies mustBe Set(EUR, USD, PLN)
      }
    }

    "getExchangeRate" must {
      "return None if not supported currency par given" in {
        market.getExchangeRate(CurrencyPair(USD, Currency("BTC"))) mustBe None
      }

      "return right ExchangeRate if direct currency pair given" in {
        market.getExchangeRate(CurrencyPair(EUR, USD)) mustBe Some(ExchangeRate(CurrencyPair(EUR, USD), 1.25))
      }

      "return right ExchangeRate if inverse currency pair given" in {
        market.getExchangeRate(CurrencyPair(PLN, EUR)) mustBe Some(ExchangeRate(CurrencyPair(PLN, EUR), 0.2))
      }

      "return right ExchangeRate if indirect currency pair given" in {
        market.getExchangeRate(CurrencyPair(USD,PLN)) mustBe Some(ExchangeRate(CurrencyPair(USD,PLN), 4.0))
      }
    }
  }
}
