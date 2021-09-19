package binders

import play.api.mvc.QueryStringBindable

object Binders {

  /**
   * Handle query parameters with square brackets>
   * E.g. "?filter=BTC&filter[]=ETH" results in Map("filter" -> Seq("BTC", "ETH")
   */
  implicit def listOfStringWithBracketsBinder(implicit listOfStringBinder: QueryStringBindable[List[String]]): QueryStringBindable[List[String]] = new QueryStringBindable[List[String]] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, List[String]]] = {
      var normalizedParams = params.withDefaultValue(Nil)
      val keyWithBrackets = key + "[]"
      if (normalizedParams.contains(keyWithBrackets)) {
        normalizedParams = normalizedParams + (key -> (normalizedParams(key) ++ normalizedParams(keyWithBrackets)))
      }

      listOfStringBinder.bind(key, normalizedParams)
    }

    override def unbind(key: String, filter: List[String]): String = {
      listOfStringBinder.unbind(key, filter)
    }
  }
}