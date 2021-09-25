package model.currencymarket.api

import model.currencymarket.{Currency, CurrencyPair, Market, MarketService}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GetExchangeRatesHandler @Inject() (marketService: MarketService, ec: ExecutionContext) {

  def apply(command: GetExchangeRatesCommand) : Future[GetExchangeRatesResult] = {
    marketService.getMarket().map(market => {
      val exchangeRates = market.getExchangeRatesForBaseCurrency(command.baseCurrency, command.quoteCurrencies)
      GetExchangeRatesResult(exchangeRates)
    })(ec)
  }
}
