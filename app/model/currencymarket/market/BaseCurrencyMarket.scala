package model.currencymarket.market

import model.currencymarket.{Currency, CurrencyPair, ExchangeRate}

case class BaseCurrencyMarket(baseCurrency: Currency, quoteCurrenciesRates: Map[Currency, BigDecimal]) extends SimpleMarket {

  override def getSupportedCurrencies: Set[Currency] = quoteCurrenciesRates.keySet + baseCurrency

  override def getExchangeRate(currencyPair: CurrencyPair): Option[ExchangeRate] = {
    getDirectOrInverseRate(currencyPair) match {
      case Some(exchangeRate: ExchangeRate) => Some(exchangeRate)
      case None => {
        /**
         * if baseCurrency = EUR and searches currencyPair = USD/PLN
         * find USD/EUR and compose it with EUR/PLN
         */
        val EUR = baseCurrency
        val USD = currencyPair.baseCurrency
        val PLN = currencyPair.quoteCurrency

        getDirectRate(CurrencyPair(EUR, USD)) match {
          case None => None
          case Some(rate_EUR_USD: ExchangeRate) => {
            val rate_USD_EUR = rate_EUR_USD.inverse
            getDirectRate(CurrencyPair(EUR, PLN)) match {
              case None => None
              case Some(rate_EUR_PLN: ExchangeRate) => Some(rate_USD_EUR compose rate_EUR_PLN)
            }
          }
        }
      }
    }
  }

  def getDirectRate(currencyPair: CurrencyPair): Option[ExchangeRate] = {
    if (currencyPair.baseCurrency == baseCurrency) {
      if (quoteCurrenciesRates.contains(currencyPair.quoteCurrency)) {
        return Some(ExchangeRate(currencyPair, quoteCurrenciesRates(currencyPair.quoteCurrency)))
      }
    }
    None
  }

  def getDirectOrInverseRate(currencyPair: CurrencyPair) : Option[ExchangeRate] = {
    getDirectRate(currencyPair) match {
      case Some(exchangeRate: ExchangeRate) => Some(exchangeRate)
      case None => getDirectRate(currencyPair.inverse) match {
        case Some(exchangeRate: ExchangeRate) => Some(exchangeRate.inverse)
        case None => None
      }
    }
  }
}