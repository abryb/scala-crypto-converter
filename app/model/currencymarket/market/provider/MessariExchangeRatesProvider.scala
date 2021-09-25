package model.currencymarket.market.provider

import model.currencymarket.market.ExchangeRatesProvider
import model.currencymarket.market.provider.messari.MessariClient
import model.currencymarket.{Currency, CurrencyPair, ExchangeRate}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
