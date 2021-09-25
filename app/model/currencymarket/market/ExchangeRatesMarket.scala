package model.currencymarket.market
import model.currencymarket._

import scala.util.control.Breaks._

case class ExchangeRatesMarket(exchangeRates: Set[ExchangeRate]) extends SimpleMarket {

  val currencyPair_exchangeRate_Map: Map[CurrencyPair, ExchangeRate] =
    exchangeRates.map(exchangeRate => (exchangeRate.pair, exchangeRate)).toMap

  override def getSupportedCurrencies: Set[Currency] = getCurrencyPairs.flatMap(_.currenciesSet)

  override def getExchangeRate(currencyPair: CurrencyPair): Option[ExchangeRate] = {
    this.getIndirectRate(currencyPair)
  }

  def getCurrencyPairs: Set[CurrencyPair] = currencyPair_exchangeRate_Map.keySet

  // TODO should handle CurrencyPair(USD,USD) ?
  def getDirectRate(currencyPair: CurrencyPair): Option[ExchangeRate] = currencyPair_exchangeRate_Map.get(currencyPair)

  def getDirectOrInverseRate(currencyPair: CurrencyPair): Option[ExchangeRate] =
    getDirectRate(currencyPair) match {
      case Some(exchangeRate: ExchangeRate) => Some(exchangeRate)
      case None => getDirectRate(currencyPair.inverse) match {
        case Some(exchangeRate: ExchangeRate) => Some(exchangeRate.inverse)
        case None => None
      }
    }

  def hasDirectRate(currencyPair: CurrencyPair): Boolean =
    currencyPair_exchangeRate_Map.contains(currencyPair)

  def hasDirectOrInverseRate(currencyPair: CurrencyPair): Boolean =
    hasDirectRate(currencyPair) || hasDirectRate(currencyPair.inverse)

  def getRelatedCurrencies(currency: Currency): Set[Currency] = {
    getCurrencyPairs
      .map(currencyPair => Set(currencyPair.quoteCurrency, currencyPair.baseCurrency))
      .filter(pairSet => pairSet.contains(currency))
      .flatten - currency
  }

  /**
   * Find indirect rate for currency pair. Max depth is 1.
   * E.g.
   * Having EUR/USD and USD/PLN rates it can return EUR/PLN
   * Having EUR/USD and USD/PLN and PLN/BTC it can return EUR/BTC rate
   */
  def getIndirectRate(currencyPair: CurrencyPair): Option[ExchangeRate] = {
    if (hasDirectOrInverseRate(currencyPair)) {
      return getDirectOrInverseRate(currencyPair)
    }

    val baseCurr = currencyPair.baseCurrency
    val quoteCurr = currencyPair.quoteCurrency
    var indirectRateResult: Option[ExchangeRate] = None

    breakable {
      for (baseRelated <- getRelatedCurrencies(currencyPair.baseCurrency)) {
        for (quoteRelated <- getRelatedCurrencies(currencyPair.quoteCurrency)) {
          // TODO refactor
          if (quoteRelated == baseRelated || this.hasDirectOrInverseRate(CurrencyPair(baseRelated, quoteRelated))) {
            val base_baseRelated_rate = this.getDirectOrInverseRate(CurrencyPair(baseCurr, baseRelated)).get.rebase(baseCurr)
            var indirectRate = base_baseRelated_rate
            if (this.hasDirectOrInverseRate(CurrencyPair(baseRelated, quoteRelated))) {
              val baseRelated_quoteRelated_rate = this.getDirectOrInverseRate(CurrencyPair(baseRelated, quoteRelated)).get.rebase(baseRelated)
              indirectRate = indirectRate.compose(baseRelated_quoteRelated_rate)
            }
            val quoteRelated_quote_rate = this.getDirectOrInverseRate(CurrencyPair(quoteCurr, quoteRelated)).get.rebase(quoteRelated)
            indirectRate = indirectRate.compose(quoteRelated_quote_rate)
            indirectRateResult =  Some(indirectRate)
            break();
          }
        }
      }
    }
    indirectRateResult
  }
}
