package model.currencymarket

import org.scalatestplus.play._

class ExchangeRateSpec extends PlaySpec {
  "An ExchangeRate" must {
    val EUR = Currency("EUR")
    val USD = Currency("USD")
    val PLN = Currency("PLN")

    "compose throws IllegalArgumentException invalid exchange rate given" in {
      val eurUsd = ExchangeRate(CurrencyPair(EUR, USD), 1.2)
      val usdPln = ExchangeRate(CurrencyPair(USD, PLN), 4.0)
      assertThrows[IllegalArgumentException] {
        usdPln.compose(eurUsd)
      }
      assertThrows[IllegalArgumentException] {
        usdPln.compose(eurUsd.inverse)
      }
    }

    "compose returns right ExchangeRate" in {
      val eurUsd = ExchangeRate(CurrencyPair(EUR, USD), 1.25)
      val usdPln = ExchangeRate(CurrencyPair(USD, PLN), 4.0)

      eurUsd.compose(usdPln) mustBe ExchangeRate(CurrencyPair(EUR, PLN), 5)
    }
  }
}
