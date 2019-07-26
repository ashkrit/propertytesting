package trading

import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, Properties}
import trade.{Trade, TradePositionAggregator}

object TradeSystemSpec extends Properties("TradingSystemSpec") {

  val listOfTrades = Gen.listOf(TradeGenerator.tradeGenerator)

  property("noOfTrades") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = new TradePositionAggregator(trades)
      val aggregatorResultChecker = new AggregationExpectedResult(trades, "trade")

      aggregator.noOfTrades() == aggregatorResultChecker.noOfTrades()
  }

  property("totalQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = new TradePositionAggregator(trades)
      val aggregatorResultChecker = new AggregationExpectedResult(trades, "trade")

      aggregator.totalQty() == aggregatorResultChecker.totalQty()
  }


  property("buyQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = new TradePositionAggregator(trades)
      val aggregatorResultChecker = new AggregationExpectedResult(trades, "trade")

      aggregator.buyQty() == aggregatorResultChecker.buyQty()
  }

  property("sellQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = new TradePositionAggregator(trades)
      val aggregatorResultChecker = new AggregationExpectedResult(trades, "trade")

      aggregator.sellQty() == aggregatorResultChecker.sellQty()
  }

  property("metricsBySecurity") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = new TradePositionAggregator(trades)
      val aggregatorResultChecker = new AggregationExpectedResult(trades, "trade")

      val expectedResult = aggregatorResultChecker.metricsBySecurity()
      val actualResult = aggregator.metricsBySecurity().sortBy { case (security, direction, _) => (security, direction) }

      actualResult == expectedResult
  }

}
