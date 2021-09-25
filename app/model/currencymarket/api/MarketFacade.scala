package model.currencymarket.api


import javax.inject.Inject
import scala.concurrent.Future

class MarketFacade @Inject() (
                             getExchangeRatesHandler: GetExchangeRatesHandler
                             ) {
  def getExchangeRates(command: GetExchangeRatesCommand) : Future[GetExchangeRatesResult] = {
    getExchangeRatesHandler.apply(command)
  }
}