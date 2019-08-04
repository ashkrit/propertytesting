package pattern

import org.scalacheck.Gen
import org.scalatest.{Matchers, PropSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/*
  Property can be proved by inverting
 */
class InverseProperty extends PropSpec with ScalaCheckPropertyChecks with Matchers {

  val purchaseGen = for {
    id <- Gen.alphaStr
    value <- Gen.choose(1, 1000)
  } yield Purchase(id, value)

  property("to CSV") {
    forAll(purchaseGen) {
      (value: Purchase) =>
        println(value)
        val result = value.toTSV()
        assert(value == Purchase.fromCsv(result))
    }
  }
}

case class Purchase(customer: String, amount: Int) {
  def toTSV(): String = {
    customer + "\t" + amount
  }
}

object Purchase {

  def fromCsv(row: String): Purchase = {
    val Array(id: String, amount: String) = row.split("\t")
    Purchase(id, amount.toInt)
  }
}
