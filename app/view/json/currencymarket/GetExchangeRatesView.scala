package view.json.currencymarket

import model.currencymarket._
import play.api.libs.json._

case class GetExchangeRatesView(baseCurrency: Currency, exchangeRates:  Seq[ExchangeRate]) {
  def toJson: JsValue = {
    new JsObject(Map(
      "source" -> JsString(baseCurrency.code),
      "rates" -> JsObject(exchangeRates.map(exchangeRate => {
        exchangeRate.pair.quoteCurrency.code -> JsNumber(exchangeRate.rate)
      }))
    ))
  }
}
