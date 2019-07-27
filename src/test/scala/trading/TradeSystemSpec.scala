package trading

import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, Properties}
import trade.{Trade, TradeAggregator, TradePositionAggregator}

object TradeSystemSpec extends Properties("TradingSystemSpec") {

  val listOfTrades = Gen.listOf(TradeGenerator.tradeGenerator)

  property("noOfTrades") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = makeTradeAggregator(trades)
      val aggregatorResultChecker = legacyAggregator(trades)

      aggregator.noOfTrades() == aggregatorResultChecker.noOfTrades()
  }

  property("totalQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = makeTradeAggregator(trades)
      val aggregatorResultChecker = legacyAggregator(trades)

      aggregator.totalQty() == aggregatorResultChecker.totalQty()
  }


  property("buyQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = makeTradeAggregator(trades)
      val aggregatorResultChecker = legacyAggregator(trades)

      aggregator.buyQty() == aggregatorResultChecker.buyQty()
  }

  property("sellQty") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = makeTradeAggregator(trades)
      val aggregatorResultChecker = legacyAggregator(trades)

      aggregator.sellQty() == aggregatorResultChecker.sellQty()
  }

  property("metricsBySecurity") = forAll(listOfTrades) {
    trades: Seq[Trade] =>

      val aggregator = makeTradeAggregator(trades)
      val aggregatorResultChecker = legacyAggregator(trades)

      val expectedResult = aggregatorResultChecker.metricsBySecurity()
      val actualResult = aggregator.metricsBySecurity().sortBy { case (security, direction, _) => (security, direction) }

      actualResult == expectedResult
  }

  private def legacyAggregator(trades: Seq[Trade]): TradeAggregator = {
    new LegacyPositionAggregator(trades, "trade")
  }

  private def makeTradeAggregator(trades: Seq[Trade]): TradeAggregator = {
    new TradePositionAggregator(trades)
  }
}
