package generator

import org.scalacheck.Gen

object Generators {

  def main(args: Array[String]): Unit = {

    val i = Gen.choose(0, 10000)
    for (times <- 0 to 5) {
      println(times + "->" + i.sample)
    }


    val pairOfNumbers = for {
      x <- Gen.choose(1, 6)
      y <- Gen.choose(1, 6)
    } yield (x, y)


    println(pairOfNumbers.sample)
  }
}
