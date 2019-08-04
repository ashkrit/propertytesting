package trading

import org.scalacheck.Gen
import trade.Trade

object TradeGenerator {


  val stocks = List("GOOG", "MSFT", "AAPL")

  val stocksGen = Gen.oneOf(stocks)
  val directionGen = Gen.oneOf(List("BUY", "SELL"))
  val qtyGen = Gen.choose(1, 1000)
  val priceGen = Gen.choose(10d, 5000d)
  val customerIdGen = Gen.choose(1, 1000000)
  val tradeIdGen = Gen.uuid

  val tradeGenerator = for {
    customerId <- customerIdGen
    tradeId <- tradeIdGen
    ticket <- stocksGen
    direction <- directionGen
    qty <- qtyGen
    price <- priceGen
  } yield Trade(customerId, tradeId.toString, ticket, direction, qty, price)

}
