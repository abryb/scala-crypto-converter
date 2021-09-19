package models

case class CurrencyExchangeOfferOption(rate: Double, amount: Double, result: Double, fee: Double)
case class CurrencyExchangeOffer(from: String, to: Map[String, CurrencyExchangeOfferOption])
