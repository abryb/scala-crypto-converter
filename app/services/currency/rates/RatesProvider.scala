package services.currency.rates

import com.google.inject.ImplementedBy
import models.CurrencyRates
import services.currency.rates.messari.MessariRatesProvider

import scala.concurrent.Future
import scala.util.Try


@ImplementedBy(classOf[MessariRatesProvider])
trait RatesProvider {

  /**
   * @param quoteCurrencies Optional list of quote currency, if Nil given returns all available currencies
   *                        If non empty list given it would be good to return quotes in same order as in list.
   */
  def getRates(baseCurrencyCode: String, quoteCurrencies: List[String]) : Future[CurrencyRates]
}
