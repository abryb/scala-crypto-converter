package model.currencymarket

import org.scalatestplus.play._

class CurrencyPairSpec extends PlaySpec {
  "A CurrencyPair" must {
    val EUR = Currency("EUR")
    val USD = Currency("USD")

    "inverse" must {
      "return inverted CurrencyPair" in {
        CurrencyPair(EUR,USD).inverse mustBe CurrencyPair(USD,EUR)
      }
    }
  }
}
