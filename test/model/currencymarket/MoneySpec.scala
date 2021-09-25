package model.currencymarket

import model.currencymarket
import org.scalatestplus.play._

class MoneySpec extends PlaySpec {
  "A Money" must {
    val USD = Currency("USD")
    val PLN = Currency("PLN")
    val EUR = Currency("EUR")

    "constructor" must {
      "not allow negative amount" in {
        assertThrows[IllegalArgumentException] {
          Money(USD, -1)
        }
      }

      "allow 0 amount" in {
        Money(USD, 0).amount mustBe 0
      }
    }

    "multiply" must {
      "multiply" in {
        val money = currencymarket.Money(USD, 10)
        money.multiply(10) mustBe currencymarket.Money(USD, 100)
        money * 10 mustBe currencymarket.Money(USD, 100)

        money.multiply(0) mustBe currencymarket.Money(USD, 0)
        money * 0.55 mustBe currencymarket.Money(USD, 5.5)
      }
    }

    "subtract" must {
      "subtract" in {
        val money = currencymarket.Money(USD, 10)
        val toSubtract = currencymarket.Money(USD, 5)

        money.subtract(toSubtract) mustBe currencymarket.Money(USD, 5)
        money - toSubtract mustBe currencymarket.Money(USD, 5)
      }

      "does not allow negative result" in {
        val usd10 = currencymarket.Money(USD, 10)
        val usd5 = currencymarket.Money(USD, 5)
        assertThrows[IllegalArgumentException] {
          usd5 - usd10
        }
      }

      "does not allow subtracting another currency" in {
        val usd10 = currencymarket.Money(USD, 10)
        val eth5 = currencymarket.Money(Currency("ETH"), 5)
        assertThrows[IllegalArgumentException] {
          usd10 - eth5
        }
      }
    }

    "exchange" must {
      "return right money" in {
        currencymarket.Money(USD, 12).exchange(ExchangeRate(CurrencyPair(EUR, USD), 1.2)) mustBe currencymarket.Money(EUR, 10)
        currencymarket.Money(EUR, 10).exchange(ExchangeRate(CurrencyPair(EUR, USD), 1.2)) mustBe currencymarket.Money(USD, 12)
      }

      "throw IllegalArgumentException" in {
        assertThrows[IllegalArgumentException] {
          currencymarket.Money(EUR, 10).exchange(ExchangeRate(CurrencyPair(PLN, USD), 1.2))
        }
      }
    }
  }
}
