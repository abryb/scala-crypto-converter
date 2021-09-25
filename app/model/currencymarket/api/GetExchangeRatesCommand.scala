package model.currencymarket.api

import model.currencymarket.Currency

case class GetExchangeRatesCommand(baseCurrency: Currency, quoteCurrencies: Option[Seq[Currency]])