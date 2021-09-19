package services.currency.rates.messari

import play.api.libs.json._

import javax.inject.Inject
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}

class MessariClient(ws: WSClient, baseUrl: String)(implicit ec: ExecutionContext) extends MessariClientInterface {

  @Inject def this(ws: WSClient, ec: ExecutionContext) = this(ws, "https://data.messari.io/api")(ec)

  def getRatesInUSD: Future[Map[String, Double]] = {
    val request: WSRequest = ws
      .url(f"$baseUrl/v2/assets?fields=symbol,metrics/market_data/price_usd&page=1&limit=100")
      .addHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()

    val ratesFuture: Future[Map[String,Double]] = futureResponse.map { response =>
      (response.json \ "data" ).as[List[JsObject]].map((x: JsValue) => {
        val code = (x \ "symbol").get.as[String].toUpperCase()
        val value = (x \ "metrics" \ "market_data" \ "price_usd").get.as[Double]
        code -> value
      }).toMap
    }(ec)

    ratesFuture
  }
}
