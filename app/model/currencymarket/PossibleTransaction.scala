package model.currencymarket

case class PossibleTransaction(selling: Money, feeRate: BigDecimal, exchangeRate: ExchangeRate) {
  require(feeRate >= 0 && feeRate <= 1, f"Transaction fee rate has to be between 0 and 1. $feeRate given.")

  val fee: Money = selling * feeRate
  val buying: Money = (selling - fee).exchange(exchangeRate)
}
