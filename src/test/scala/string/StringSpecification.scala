package string

import org.scalatest.{Matchers, PropSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import string.BetterStringUtil.{contains, truncate}
import org.scalacheck.Gen.{alphaStr, listOf, numStr}

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


  property("contains-with-true-result") {
    forAll {
      (value1: String, value2: String) =>
        val combineText = value1 + value2
        val result = contains(combineText, value2)
        assert(result == true)
    }
  }


  property("tokens") {
    forAll(listOf(alphaStr), numStr) {

      (values, token) =>
        val mergedValues = values.mkString(token)
        val tokens = BetterStringUtil.tokens(mergedValues, token)
        assert(tokens.toList == values)

    }
  }


}
