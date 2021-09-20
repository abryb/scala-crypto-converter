package services.currency.exchange

import models.{CurrencyExchangeOffer, CurrencyExchangeOfferOption, CurrencyRates}
import services.currency.rates.RatesProvider

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DefaultCurrencyExchangeOfferMaker @Inject()(ratesProvider: RatesProvider)
    extends CurrencyExchangeOfferMaker {

  override def getOffer(from: String, to: List[String], amount: Double): Future[CurrencyExchangeOffer] = {

    ratesProvider.getRates(from, to).map((currencyRates : CurrencyRates) => {
      val rates = currencyRates.rates
      val options = to
        .filter(rates.contains)
        .map(currency => {

          val rate = rates(currency)
          val fee = amount * 0.01
          val result = rate * (amount - fee)
          (currency, CurrencyExchangeOfferOption(rate, amount, result, fee))
        })
      CurrencyExchangeOffer(from, options.toMap)
    })
  }
}
