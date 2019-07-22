package list

import org.scalacheck.Properties
import org.scalacheck.Prop.{forAll, throws}

object ListSpec extends Properties("ListSpecs") {

  property("list tail") = forAll {
    (x: Int, xs: List[Int]) =>
      (x :: xs).tail == xs
  }

  property("list head") = forAll {
    xs: List[Int] =>
      if (xs.isEmpty) {
        throws(classOf[NoSuchElementException]) {
          xs.head
        }
      }
      else
        xs.head == xs(0)
  }

}
