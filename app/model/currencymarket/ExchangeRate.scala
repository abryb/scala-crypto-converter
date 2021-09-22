package model.currencymarket

case class ExchangeRate(pair: CurrencyPair, rate: BigDecimal) {
  def inverse : ExchangeRate = ExchangeRate(pair.inverse, 1.0 / rate)

  def rebase(currency: Currency) : ExchangeRate = {
    if (this.pair.baseCurrency == currency) {
      return this
    }
    this.inverse
  }

  /**
   * @return new exchange rate e.g.
   *         (EUR/USD,1.25) compose (USD/PLN,4)  => (EUR/PLN,5)
   */
  def compose(that: ExchangeRate) : ExchangeRate = {
    require(that.pair.baseCurrency == this.pair.quoteCurrency, f"Invalid exchange rate multiplication! Can't compose $this with $that")
    ExchangeRate(CurrencyPair(this.pair.baseCurrency, that.pair.quoteCurrency), this.rate * that.rate)
  }
}
