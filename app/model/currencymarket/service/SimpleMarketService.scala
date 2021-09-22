package model.currencymarket.service

import model.currencymarket.Market

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SimpleMarketService @Inject()(exchangeRatesProvider: ExchangeRatesProvider, ec: ExecutionContext) {
  // TODO is this ExecutionContext service same as global?
  def getMarket() : Future[Market] = {
    exchangeRatesProvider.getExchangeRates.map(exchangeRatesSet =>  Market(exchangeRatesSet))(ec)
  }
}
