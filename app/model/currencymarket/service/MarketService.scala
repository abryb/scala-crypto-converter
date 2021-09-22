package model.currencymarket.service

import com.google.inject.ImplementedBy
import model.currencymarket.Market

import scala.concurrent.Future

@ImplementedBy(classOf[SimpleMarketService])
trait MarketService {
  def getMarket() : Future[Market]
}
