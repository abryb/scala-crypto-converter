package model.currencymarket

import org.scalatestplus.play._

class MarketSpec extends PlaySpec {
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

    "construct" in {
      val market =  new Market(exchangeRates)
      market.getExchangeRates mustBe exchangeRates
    }

    "hasDirectRate test" in {
      val market =  new Market(exchangeRates)
      market.hasDirectRate(USD_PLN) mustBe true
      market.hasDirectRate(USD_PLN.inverse) mustBe false
    }

    "hasDirectOrInverseRate test" in {
      val market =  new Market(exchangeRates)
      market.hasDirectOrIndirectRate(USD_PLN) mustBe true
      market.hasDirectOrIndirectRate(USD_PLN.inverse) mustBe true
    }

    "findRelatedCurrencies" in {
      val market =  new Market(exchangeRates)
      market.getRelatedCurrencies(USD) mustBe Set(PLN, EUR)
      market.getRelatedCurrencies(PLN) mustBe Set(USD)
      market.getRelatedCurrencies(EUR) mustBe Set(USD)
    }

    "getDirectOrInverseRate should return right rate" in {
      val market =  new Market(exchangeRates)
      val PLN_USD = USD_PLN.inverse

      market.getDirectOrInverseRate(PLN_USD) mustBe Some(ExchangeRate(PLN_USD, 0.25))
    }

    "getIndirectRate should returns right rate" in {
      val market =  new Market(Set(
        ExchangeRate(EUR_USD, 1.25),
        ExchangeRate(USD_PLN, 4.0),
        ExchangeRate(PLN_BTC, 0.5)
      ))
      val EUR_BTC = CurrencyPair(EUR, BTC)
      market.getIndirectRate(EUR_BTC) mustBe Some(ExchangeRate(EUR_BTC, 2.5))
    }

    "getIndirectRate should returns none if can't create indirect rate" in {
      val market =  new Market(Set(
        ExchangeRate(EUR_USD, 1.25),
        ExchangeRate(PLN_BTC, 0.5)
      ))
      val EUR_BTC = CurrencyPair(EUR, BTC)
      market.getIndirectRate(EUR_BTC) mustBe None
    }

    "getIndirectRates returns right rates" in {
      val rates = Set(
        ExchangeRate(EUR_USD, 1.25),
        ExchangeRate(PLN_BTC, 0.5)
      )
      val market =  new Market(rates)
      val EUR_BTC = CurrencyPair(EUR, BTC)
      market.getIndirectRates(Seq(EUR_USD, PLN_BTC)) mustBe rates.toSeq
    }

  }
}
