package model.currencymarket

import model.currencymarket.exception.NotSupportedCurrencyPairException

trait Market {
  def getSupportedCurrencies : Set[Currency]

  def getExchangeRate(currencyPair: CurrencyPair) : Option[ExchangeRate]

  def hasExchangeRateFor(currencyPair: CurrencyPair) : Boolean

  /**
   * @throws NotSupportedCurrencyPairException if quoteCurrencies given and any of quoteCurrency can be handled
   */
  @throws(classOf[NotSupportedCurrencyPairException])
  def getExchangeRatesForBaseCurrency(baseCurrency : Currency, quoteCurrencies : Option[Seq[Currency]]): Seq[ExchangeRate]
}