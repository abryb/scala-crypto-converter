package model.currencymarket.market

import com.google.inject.ImplementedBy
import model.currencymarket.ExchangeRate
import model.currencymarket.market.provider.MessariExchangeRatesProvider

import scala.concurrent.Future

@ImplementedBy(classOf[MessariExchangeRatesProvider])
trait ExchangeRatesProvider {
  def getExchangeRates: Future[Set[ExchangeRate]]
}
