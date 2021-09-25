package model.currencymarket.market.provider.messari

import com.google.inject.ImplementedBy

import scala.concurrent.Future

@ImplementedBy(classOf[WsMessariClient])
trait MessariClient {
  def getRatesInUSD: Future[Map[String, BigDecimal]]
}
