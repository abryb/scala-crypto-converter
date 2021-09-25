package model.currencymarket

case class ExchangeRate(pair: CurrencyPair, rate: BigDecimal) {

  def inverse : ExchangeRate = ExchangeRate(pair.inverse, 1.0 / rate)

  /**
   * @return ExchangeRate e.g.
   *         ExchangeRate(EUR/USD,1.25).rebase(EUR) => ExchangeRate(EUR/USD,1.25)
   *         ExchangeRate(EUR/USD,1.25).rebase(USD) => ExchangeRate(USD/EUR,0.8)
   */
  def rebase(currency: Currency) : ExchangeRate = {
    require(currency == this.pair.baseCurrency || currency  == this.pair.quoteCurrency, f"Can't rebase ExchangeRate $this with Currency $currency")
    if (this.pair.baseCurrency == currency) {
      return this
    }
    this.inverse
  }

  /**
   * @return ExchangeRate e.g.
   *         ExchangeRate(EUR/USD,1.25) compose ExchangeRate(USD/PLN,4)  => ExchangeRate(EUR/PLN,5)
   */
  def compose(that: ExchangeRate) : ExchangeRate = {
    require(that.pair.baseCurrency == this.pair.quoteCurrency, f"Invalid exchange rate multiplication! Can't compose $this with $that")
    ExchangeRate(CurrencyPair(this.pair.baseCurrency, that.pair.quoteCurrency), this.rate * that.rate)
  }
}
