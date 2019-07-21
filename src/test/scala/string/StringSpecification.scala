package string

import org.scalatest.{Matchers, PropSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import string.BetterStringUtil.truncate

class StringSpecification extends PropSpec with ScalaCheckPropertyChecks with Matchers {

  property("truncate") {

    forAll { (value: String, n: Int) =>

      val result = truncate(value, n)

      assert(
        (n < 0 && result == "") ||
          (n >= value.length && result == value) ||
          (n <= value.length && result == value.take(n) + "...")
      )
    }
  }

}
