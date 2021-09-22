package controllers.api.currencymarket

import model.currencymarket.api._
import model.currencymarket.{Currency, MarketFacade}
import play.api.libs.json.Json
import play.api.mvc._
import view.json.currencymarket._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CurrencyController @Inject()(
                                  cc: ControllerComponents,
                                  marketFacade: MarketFacade
                                  ) extends AbstractController(cc) {

  def getExchangeRates(code: String, filter: List[String]): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    // TODO wrap all in future
    // TODO move prepare params somewhere else
    val baseCurrency = Currency(code)
    val quoteCurrencies = filter match {
      case Nil => None
      case currencyCodes => Some(currencyCodes.map(Currency))
    }
    val params = GetExchangeRatesParams(baseCurrency = baseCurrency, quoteCurrencies = quoteCurrencies)
    marketFacade
      .getExchangeRates(GetExchangeRatesParams(baseCurrency = baseCurrency, quoteCurrencies = quoteCurrencies))
      .map((result: GetExchangeRatesResult) => {
        // TODO exception handling?
        // TODO better view handling
        Ok((GetExchangeRatesView(params.baseCurrency, result.exchangeRates).toJson))
      })
  }


  def createCurrencyExchangeOffers: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      Ok((Json.parse("{}")))
    }
  }
}

