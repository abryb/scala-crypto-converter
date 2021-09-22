package model.currencymarket

case class Currency(code: String) {
  require(code == code.toUpperCase(), f"Currency code has to be in upper case! $code given!")
}


