package services.currency.exchange

import com.google.inject.ImplementedBy
import models.CurrencyExchangeOffer

import scala.concurrent.Future

@ImplementedBy(classOf[DefaultCurrencyExchangeOfferMaker])
trait CurrencyExchangeOfferMaker {
  def getOffer(from: String, to: List[String], amount: Double): Future[CurrencyExchangeOffer]
}
