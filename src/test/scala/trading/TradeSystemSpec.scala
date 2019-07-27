package trading

import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, Properties}
import trade.v2.OptimizedTradePositionAggregator
import trade.{Trade, TradeAggregator, TradePositionAggregator}

object TradeSystemSpec extends Properties("TradingSystemSpec") {

  val listOfTrades = Gen.listOf(TradeGenerator.tradeGenerator)

  property("noOfTrades") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val newAggregator = makeTradeAggregator(trades)
      val legacySystem = makeLegacyAggregator(trades)

      newAggregator.noOfTrades() == legacySystem.noOfTrades()
  }

  property("totalQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val newAggregator = makeTradeAggregator(trades)
      val legacySystem = makeLegacyAggregator(trades)

      newAggregator.totalQty() == legacySystem.totalQty()
  }


  property("buyQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val newAggregator = makeTradeAggregator(trades)
      val legacySystem = makeLegacyAggregator(trades)

      newAggregator.buyQty() == legacySystem.buyQty()
  }

  property("sellQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val newAggregator = makeTradeAggregator(trades)
      val legacySystem = makeLegacyAggregator(trades)

      newAggregator.sellQty() == legacySystem.sellQty()
  }

  property("metricsBySecurity") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val newAggregator = makeTradeAggregator(trades)
      val legacySystem = makeLegacyAggregator(trades)

      //println(s"Trade Count ${trades.size} -> ${trades}")
      val expectedResult = newAggregator.metricsBySecurity().sortBy { case (security, direction, _) => (security, direction) }
      val actualResult = legacySystem.metricsBySecurity().sortBy { case (security, direction, _) => (security, direction) }

      val result = actualResult == expectedResult
      if (!result) {
        println(s"Expected ${expectedResult} -> Actual ${actualResult}")
      }
      result
  }

  private def makeLegacyAggregator(trades: Seq[Trade]): TradeAggregator = {
    new LegacyPositionAggregator(trades, "trade")
  }

  private def makeTradeAggregator(trades: Seq[Trade]): TradeAggregator = {
    new OptimizedTradePositionAggregator(trades)
  }
}
