package binders

import org.scalatestplus.play._

class BindersSpec extends PlaySpec {
  "A listOfStringWithBracketsBinder" must {

    "bind None" in {
      val binder = Binders.listOfStringWithBracketsBinder
      val params = Map("foobar" -> List("FOO"))
      val result = binder.bind("filter", params)
      result mustBe Some(Right(Nil))
    }

    "bind key" in {
      val binder = Binders.listOfStringWithBracketsBinder
      val params = Map("filter" -> List("FOO"))
      val result = binder.bind("filter", params)
      result mustBe Some(Right(List("FOO")))
    }

    "bind key[]" in {
      val binder = Binders.listOfStringWithBracketsBinder
      val params = Map("filter[]" -> List("FOO"))
      val result = binder.bind("filter[]", params)
      result mustBe Some(Right(List("FOO")))
    }

    "bind key and key[]" in {
      val binder = Binders.listOfStringWithBracketsBinder
      val params = Map(
        "filter" -> List("FOO"),
        "filter[]" -> List("BAR")
      )
      val result = binder.bind("filter", params)
      result mustBe Some(Right(List("FOO", "BAR")))
    }
  }
}
