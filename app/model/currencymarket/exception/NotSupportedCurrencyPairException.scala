package model.currencymarket.exception

import model.currencymarket.CurrencyPair

case class NotSupportedCurrencyPairException(currencyPair: CurrencyPair, message: String) extends CurrencyMarketException(message) {
  def this(currencyPair: CurrencyPair) = this(currencyPair, f"CurrencyPair $currencyPair is not supported!")
}
