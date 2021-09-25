package model.currencymarket

import org.scalatestplus.play._

class CurrencySpec extends PlaySpec {
  "A Currency" must {
    "constructor" must {
      "not allow not upper case code" in {
        assertThrows[IllegalArgumentException] {
          Currency("usd")
        }
      }

      "allow upper case code" in {
        Currency("USD").code mustBe "USD"
      }
    }
  }
}
