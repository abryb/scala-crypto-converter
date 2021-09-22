package model.currencymarket.service.provider

import model.currencymarket.service.provider.messari.{MessariClient, WsMessariClient}
import model.currencymarket.service.ExchangeRatesProvider
import model.currencymarket.{Currency, CurrencyPair, ExchangeRate, Market}

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class MessariExchangeRatesProvider @Inject() (messariClient: MessariClient) extends ExchangeRatesProvider {
  override def getExchangeRates: Future[Set[ExchangeRate]] = {
    messariClient.getRatesInUSD.map(ratesInUSD => {
      ratesInUSD.map{
        case(quoteCurrencyCode, unitPriceInUSD) =>
          ExchangeRate(CurrencyPair(Currency(quoteCurrencyCode), Currency("USD")), unitPriceInUSD)
      }.toSet
    })
  }
}
