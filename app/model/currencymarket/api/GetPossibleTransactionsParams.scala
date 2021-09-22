package model.currencymarket.api

import model.currencymarket.{Currency, Money}

case class GetPossibleTransactionsParams(from: Money, to: Seq[Currency])