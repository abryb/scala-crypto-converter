package services.currency.rates.messari

import com.google.inject.ImplementedBy

import scala.concurrent.Future

@ImplementedBy(classOf[MessariClient])
trait MessariClientInterface {
  def getRatesInUSD: Future[Map[String, Double]]
}
