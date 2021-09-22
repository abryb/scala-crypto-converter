package model.currencymarket.api

import model.currencymarket.ExchangeRate

case class GetExchangeRatesResult(exchangeRates: Seq[ExchangeRate])