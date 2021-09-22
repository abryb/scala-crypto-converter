package model.currencymarket

import model._
import org.scalatestplus.play._

class PossibleTransactionSpec extends PlaySpec {
  "A Transaction" must {
    val BTC = Currency("BTC")
    val ETH = Currency("ETH")
    val btc10 = Money(BTC, 10)
    val btcEthRate = ExchangeRate(CurrencyPair(BTC, ETH), 10)

    "allow feeRate between 0 and 1" in {
      PossibleTransaction(btc10, 0.5, btcEthRate).feeRate mustBe 0.5
    }


    "not allow feeRate < 0" in {
      assertThrows[IllegalArgumentException] {
        PossibleTransaction(btc10, -0.01, btcEthRate)
      }
    }

    "not allow feeRate > 1" in {
      assertThrows[IllegalArgumentException] {
        PossibleTransaction(btc10, 1.01, btcEthRate)
      }
    }

    "calculate fee" in {
      PossibleTransaction(btc10, 0.1, btcEthRate).fee mustBe Money(BTC, 1)
    }

    "calculate result" in {
      PossibleTransaction(btc10, 0.0, btcEthRate).buying mustBe Money(ETH, 100)
    }
  }
}
