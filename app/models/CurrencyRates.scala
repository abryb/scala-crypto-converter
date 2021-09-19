package models

/**
 * @param baseCurrencyCode code of base currency
 * @param rates Map of rates
 *              Key: String quote currency code
 *              Value: Double exchange rate for single unit of base currency
 */
case class CurrencyRates(baseCurrencyCode: String, rates: Map[String, Double])

