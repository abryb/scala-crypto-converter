package model.currencymarket.market

import model.currencymarket.{Currency, CurrencyPair, ExchangeRate, Market}
import model.currencymarket.exception.NotSupportedCurrencyPairException


trait SimpleMarket extends Market {
  def getSupportedCurrencies: Set[Currency]

  def getExchangeRate(currencyPair: CurrencyPair): Option[ExchangeRate]

  // default methods

  override def hasExchangeRateFor(currencyPair: CurrencyPair): Boolean = {
    getExchangeRate(currencyPair) match {
      case Some(exchangeRate: ExchangeRate) => true
      case None => false
    }
  }

  @throws(classOf[NotSupportedCurrencyPairException])
  override def getExchangeRatesForBaseCurrency(baseCurrency: Currency, quoteCurrenciesFilter: Option[Seq[Currency]]): Seq[ExchangeRate] = {
    quoteCurrenciesFilter match {
      case None => getSupportedCurrencies.toSeq.flatMap(quoteCurrency => {
        getExchangeRate(CurrencyPair(baseCurrency, quoteCurrency)) match {
          case None => Nil
          case Some(exchangeRate: ExchangeRate) => List(exchangeRate)
        }
      })
      case Some(quoteCurrencies) => quoteCurrencies.flatMap(quoteCurrency => {
        val currencyPair = CurrencyPair(baseCurrency, quoteCurrency)
        getExchangeRate(currencyPair) match {
          case None => throw new NotSupportedCurrencyPairException(currencyPair)
          case Some(exchangeRate: ExchangeRate) => List(exchangeRate)
        }
      })
    }
  }
}
