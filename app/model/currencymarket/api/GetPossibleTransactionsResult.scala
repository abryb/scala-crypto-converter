package model.currencymarket.api

import model.currencymarket.PossibleTransaction

case class GetPossibleTransactionsResult(possibleTransactions: Seq[PossibleTransaction])