package model.currencymarket

case class CurrencyPair(baseCurrency: Currency, quoteCurrency: Currency) {
  def inverse : CurrencyPair = CurrencyPair(quoteCurrency, baseCurrency)

  def currenciesSet : Set[Currency] = Set(baseCurrency, quoteCurrency)
}