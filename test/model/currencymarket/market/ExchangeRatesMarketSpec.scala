package model.currencymarket.market

import model.currencymarket.{Currency, CurrencyPair, ExchangeRate}
import org.scalatestplus.play._

class ExchangeRatesMarketSpec extends PlaySpec {
  "A Market" must {
    val USD = Currency("USD")
    val EUR = Currency("EUR")
    val PLN = Currency("PLN")
    val BTC = Currency("BTC")

    val EUR_USD = CurrencyPair(EUR, USD)
    val USD_PLN = CurrencyPair(USD, PLN)
    val PLN_BTC = CurrencyPair(PLN,BTC)

    val exchangeRates = Set(
      ExchangeRate(USD_PLN, 4.0),
      ExchangeRate(EUR_USD,  1.25)
    )

    "hasDirectRate" must {
      "return true if supported direct currency pair given" in {
        val market = ExchangeRatesMarket(exchangeRates)
        market.hasDirectRate(USD_PLN) mustBe true
      }

      "return false if not supported direct currency pair given" in {
        val market = ExchangeRatesMarket(exchangeRates)
        market.hasDirectRate(USD_PLN.inverse) mustBe false
      }
    }

    "hasDirectOrInverseRate" must {
      val market = ExchangeRatesMarket(exchangeRates)

      "return true if supported direct rate given" in {
        market.hasDirectOrInverseRate(USD_PLN) mustBe true
      }
      "return true if supported inverse rate given" in {
        market.hasDirectOrInverseRate(CurrencyPair(PLN, USD)) mustBe true
      }
      "return false if not supported direct nor inverse rate given" in {
        market.hasDirectOrInverseRate(PLN_BTC) mustBe false
      }
    }

    "hasDirectOrInverseRate" must {
      "be valid" in {
        val market = ExchangeRatesMarket(exchangeRates)
        market.hasDirectOrInverseRate(USD_PLN) mustBe true
        market.hasDirectOrInverseRate(USD_PLN.inverse) mustBe true
      }
    }

    "findRelatedCurrencies" must {
      "be valid" in {
        val market = ExchangeRatesMarket(exchangeRates)
        market.getRelatedCurrencies(USD) mustBe Set(PLN, EUR)
        market.getRelatedCurrencies(PLN) mustBe Set(USD)
        market.getRelatedCurrencies(EUR) mustBe Set(USD)
      }
    }

    "getDirectOrInverseRate" must {
      "be valid" in {
        val market = ExchangeRatesMarket(exchangeRates)
        val PLN_USD = USD_PLN.inverse
        market.getDirectOrInverseRate(PLN_USD) mustBe Some(ExchangeRate(PLN_USD, 0.25))
      }
    }

    "getIndirectRate" must {
      "return right rate" in {
        val market = ExchangeRatesMarket(Set(
          ExchangeRate(EUR_USD, 1.25),
          ExchangeRate(USD_PLN, 4.0),
          ExchangeRate(PLN_BTC, 0.5)
        ))
        val EUR_BTC = CurrencyPair(EUR, BTC)
        market.getIndirectRate(EUR_BTC) mustBe Some(ExchangeRate(EUR_BTC, 2.5))
      }
      "should return None if can't create indirect rate" in {
        val market = ExchangeRatesMarket(Set(
          ExchangeRate(EUR_USD, 1.25),
          ExchangeRate(PLN_BTC, 0.5)
        ))
        val EUR_BTC = CurrencyPair(EUR, BTC)
        market.getIndirectRate(EUR_BTC) mustBe None
      }
    }
  }
}
