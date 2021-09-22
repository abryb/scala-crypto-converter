package model.currencymarket.service

import com.google.inject.ImplementedBy
import model.currencymarket.{ExchangeRate, Market}
import model.currencymarket.service.provider.MessariExchangeRatesProvider

import scala.concurrent.Future

@ImplementedBy(classOf[MessariExchangeRatesProvider])
trait ExchangeRatesProvider {
  def getExchangeRates: Future[Set[ExchangeRate]]
}
