package model.currencymarket

import com.google.inject.ImplementedBy
import model.currencymarket.market.ExchangeRatesMarketService

import scala.concurrent.Future

@ImplementedBy(classOf[ExchangeRatesMarketService])
trait MarketService {
  def getMarket() : Future[Market]
}
