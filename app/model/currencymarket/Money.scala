package model.currencymarket

case class Money(currency: Currency, amount: BigDecimal) {
  require(amount >= 0, "Money amount has to be greater than 0.")

  def multiply(that: BigDecimal): Money = {
    Money(currency, amount * that)
  }

  def *(that: BigDecimal): Money = multiply(that)

  def subtract(that: Money): Money = {
    require(that.currency == this.currency, "Can't subtract money with different currency.")
    require(that.amount <= this.amount, f"Can't subtract $that from $this. It results in negative amount.")
    Money(currency, amount - that.amount)
  }

  def -(that: Money): Money = subtract(that)

  def exchange(exchangeRate: ExchangeRate): Money = {
    val rate = exchangeRate.rate
    exchangeRate.pair match {
      case CurrencyPair(this.currency, targetCurrency) => Money(targetCurrency, amount * rate)
      case CurrencyPair(targetCurrency, this.currency) => exchange(exchangeRate.inverse)
      case _ => throw new IllegalArgumentException(s"Can not exchange money $this with currency pair ${exchangeRate.pair}")
    }
  }
}
