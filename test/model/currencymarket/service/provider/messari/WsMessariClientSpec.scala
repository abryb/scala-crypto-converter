package model.currencymarket.service.provider.messari

import org.scalatestplus.play._
import play.api.libs.json._
import play.api.mvc._
import play.api.routing.sird._
import play.api.test._
import play.core.server.Server

import scala.concurrent.Await
import scala.concurrent.duration._

class WsMessariClientSpec extends PlaySpec {
  import scala.concurrent.ExecutionContext.Implicits.global

  "MessariClient" should {
    "get all rates " in {
      Server.withRouterFromComponents() { components =>
        import Results._
        import components.{defaultActionBuilder => Action}
        {
          case GET(p"/v2/assets") =>
            Action {
              Ok(Json.parse(
                """
                  |{
                  |  "status": {
                  |    "elapsed":69,
                  |    "timestamp":"2021-09-19T13:02:34.659143565Z"
                  |  },
                  |  "data": [
                  |    {
                  |      "id":"1e31218a-e44e-4285-820c-8282ee222035",
                  |      "slug":"bitcoin",
                  |      "symbol":"BTC",
                  |      "metrics":{
                  |        "market_data":{
                  |          "price_usd":47289.12710924173
                  |        }
                  |       }
                  |    },
                  |    {
                  |      "id":"21c795f5-1bfd-40c3-858e-e9d7e820c6d0",
                  |      "slug":"ethereum",
                  |      "symbol":"ETH",
                  |      "metrics": {
                  |        "market_data": {
                  |          "price_usd":3342.1770216696978
                  |        }
                  |      }
                  |    }
                  |  ]
                  |}
                  |""".stripMargin))
            }
        }
      } { implicit port =>
        WsTestClient.withClient { client =>
          val result = Await.result(new WsMessariClient(client, "").getRatesInUSD, 10.seconds)
          result mustBe Map("BTC" -> 47289.12710924173 ,"ETH" -> 3342.1770216696978)
        }
      }
    }
  }
}
