package model.currencymarket.api

import model.currencymarket.Currency

case class GetExchangeRatesParams(baseCurrency: Currency, quoteCurrencies: Option[Seq[Currency]])