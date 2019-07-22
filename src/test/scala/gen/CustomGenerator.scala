package gen

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

object CustomGenerator {

  case class Trade(ticker: String, buyOrSell: String, unit: Int, price: Double)


  val buySell = List("BUY", "SELL")
  val stocks = List("GOOG", "MSFT", "AAPL")

  val someStocks = Gen.oneOf(stocks)
  val buyOrSell = Gen.oneOf(buySell)
  val unitGen = Gen.choose(1, 1000)
  val priceGen = Gen.choose(10d, 500d)

  val tradeGenerator = for {
    ticket <- someStocks
    op <- buyOrSell
    unit <- unitGen
    price <- priceGen
  } yield Trade(ticket, op, unit, price)


  def main(args: Array[String]): Unit = {


    for (times <- 0 to 10) {
      //println(s"Trade No ${times} -> ${tradeGenerator.sample.get}")
    }


    val trades = forAll(tradeGenerator) { t: Trade =>
      stocks.contains(t.ticker) &&
        t.unit > 0 &&
        t.price > 0
    }

    trades.check()
  }

}
