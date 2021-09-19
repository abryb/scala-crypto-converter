package services.currency.rates.messari

import exception.NotSupportedCurrencyException
import models.CurrencyRates
import services.currency.rates.RatesProvider

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MessariRatesProvider @Inject()(messariApi: MessariClientInterface) extends RatesProvider {

  override def getRates(baseCurrencyCode: String, filter: List[String]): Future[CurrencyRates] = {
    val baseCode = baseCurrencyCode.toUpperCase()

    messariApi.getRatesInUSD.map(ratesInUSD => {
      if (!ratesInUSD.contains(baseCode)) {
        throw new NotSupportedCurrencyException(f"Currency with code $baseCurrencyCode is not supported!")
      }
      val codeValue = ratesInUSD(baseCode)
      val rates = (filter match {
        case Nil => ratesInUSD
        case x =>
          filter.map(_.toUpperCase).filter(code => ratesInUSD.contains(code)).map(code => {
            (code, ratesInUSD(code))
          }).toMap
      }).map { case (key, value) => (key, codeValue / value) }
      CurrencyRates(baseCode, rates)
    })
  }
}
