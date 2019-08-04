package pattern

import org.scalatest.{Matchers, PropSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.util.Random

/*
  Order of element does not matter. Final result will be same.
 */
class DifferentInputOrderButSameOutput extends PropSpec with ScalaCheckPropertyChecks with Matchers {


  property("sumvalues") {
    forAll { (value: Array[Int]) =>
      val result = GPUMaths.sum(value: _*)
      val expectedValue = Random.shuffle(value.toList).sum
      assert(result == expectedValue)
    }
  }


  property("sortvalues") {

    forAll { (v1: Int, v2: Int, v3: Int) =>

      val result = GPUMaths.sort(List(v1, v2, v3))
      val expected1 = List(v3, v1, v2).sorted
      val expected2 = List(v2, v1, v3).sorted

      assert(result == expected1 && result == expected2)
    }
  }


}
