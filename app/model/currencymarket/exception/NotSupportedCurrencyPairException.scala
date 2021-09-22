package model.currencymarket.exception

import model.currencymarket.CurrencyPair

class NotSupportedCurrencyPairException(message: String) extends CurrencyMarketException(message) {
  def this(currencyPair: CurrencyPair) = this(f"CurrencyPair $currencyPair is not supported!")
}
