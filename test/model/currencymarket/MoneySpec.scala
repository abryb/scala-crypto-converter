package model.currencymarket

import model.currencymarket._
import org.scalatestplus.play._

class MoneySpec extends PlaySpec {
  "A Money" must {
    val USD = Currency("USD")
    val PLN = Currency("PLN")
    val EUR = Currency("EUR")

    "not allow negative amount" in {
      assertThrows[IllegalArgumentException] {
        Money(USD, -1)
      }
    }

    "allow 0 amount" in {
      Money(USD, 0).amount mustBe 0
    }

    "multiply" in {
      val money = Money(USD, 10)
      money.multiply(10) mustBe Money(USD, 100)
      money * 10 mustBe Money(USD, 100)

      money.multiply(0) mustBe Money(USD, 0)
      money * 0.55 mustBe Money(USD, 5.5)
    }

    "subtract" in {
      val money = Money(USD, 10)
      val toSubtract = Money(USD, 5)

      money.subtract(toSubtract) mustBe Money(USD, 5)
      money - toSubtract mustBe Money(USD, 5)
    }

    "subtract does not allow negative result" in {
      val usd10 = Money(USD, 10)
      val usd5 = Money(USD, 5)
      assertThrows[IllegalArgumentException] {
        usd5 - usd10
      }
    }

    "subtract does not allow subtracting another currency" in {
      val usd10 = Money(USD, 10)
      val eth5 = Money(Currency("ETH"), 5)
      assertThrows[IllegalArgumentException] {
        usd10 - eth5
      }
    }

    "exchange returns right money" in {
      Money(USD, 12).exchange(ExchangeRate(CurrencyPair(EUR, USD), 1.2)) mustBe Money(EUR, 10)
      Money(EUR, 10).exchange(ExchangeRate(CurrencyPair(EUR, USD), 1.2)) mustBe Money(USD, 12)
    }

    "exchange throws IllegalArgumentException" in {
      assertThrows[IllegalArgumentException] {
        Money(EUR, 10).exchange(ExchangeRate(CurrencyPair(PLN, USD), 1.2))
      }
    }
  }
}
