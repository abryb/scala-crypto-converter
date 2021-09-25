package model.currencymarket.market

import model.currencymarket.{Market, MarketService}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ExchangeRatesMarketService @Inject()(exchangeRatesProvider: ExchangeRatesProvider, ec: ExecutionContext) extends MarketService {
  // TODO is this ExecutionContext service same as global?
  def getMarket() : Future[Market] = {
    exchangeRatesProvider.getExchangeRates.map(exchangeRatesSet => ExchangeRatesMarket(exchangeRatesSet))(ec)
  }
}
