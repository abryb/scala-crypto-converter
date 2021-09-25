package model.currencymarket.market.provider.messari

import play.api.libs.json._
import play.api.libs.ws._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class WsMessariClient(ws: WSClient, baseUrl: String)(implicit ec: ExecutionContext) extends MessariClient {

  @Inject def this(ws: WSClient, ec: ExecutionContext) = this(ws, "https://data.messari.io/api")(ec)

  def getRatesInUSD: Future[Map[String, BigDecimal]] = {
    val request: WSRequest = ws
      .url(f"$baseUrl/v2/assets?fields=symbol,metrics/market_data/price_usd&page=1&limit=50")
      .addHttpHeaders("Accept" -> "application/json")

    request.get().map { response =>
      (response.json \ "data" ).as[List[JsObject]].map((x: JsValue) => {
        val code = (x \ "symbol").get.as[String].toUpperCase()
        val value = (x \ "metrics" \ "market_data" \ "price_usd").get.as[Double]
        code -> BigDecimal(value)
      }).toMap
    }(ec)
  }
}
