package model.currencymarket

import model.currencymarket.service.{ExchangeRatesProvider, MarketService}
import model.currencymarket.api.{GetExchangeRatesResult, _}

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Service
 */
class MarketFacade @Inject() (marketService: MarketService) {

  def getExchangeRates(params: GetExchangeRatesParams) : Future[GetExchangeRatesResult] = {
    marketService.getMarket().map(market => {
      val exchangeRates = params.quoteCurrencies match {
        case None => market.getIndirectRates(market.getCurrencyPairs.toSeq)
        case Some(quoteCurrenciesSeq) => market.getIndirectRates(quoteCurrenciesSeq.map(quoteCurr => CurrencyPair(params.baseCurrency, quoteCurr)))
      }
      GetExchangeRatesResult(exchangeRates)
    })
  }
}