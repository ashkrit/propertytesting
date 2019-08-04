package trade.v2

import trade.{Metrics, Position, Trade, TradeAggregator}

class OptimizedTradePositionAggregator(trades: Seq[Trade]) extends TradeAggregator {

  val metrics: Seq[(Position, Metrics)] = calculate()

  @Override def noOfTrades(): Int = {
    metrics.map(_._2.count).sum
  }

  @Override def totalQty(): Long = {
    metrics
      .map { case (_, metrics) => metrics.totalQty }
      .sum
  }

  @Override def buyQty(): Long = {
    metrics
      .filter { case (key, _) => key.direction == "BUY" }
      .map { case (_, metrics) => metrics.totalQty }
      .sum
  }

  @Override def sellQty(): Long = {
    metrics
      .filter { case (key, _) => key.direction == "SELL" }
      .map { case (_, metrics) => metrics.totalQty }
      .sum

  }

  @Override def metricsBySecurity(): Seq[(String, String, Double)] = {

    metrics
      .map { case (key, metric) => (key.sec, key.direction, metric.tradeAmount) }
      .toList

  }

  def calculate(): Seq[(Position, Metrics)] = {

    trades.map {
      trade => (toPositionKey(trade), Metrics(1, trade.qty, trade.qty * trade.price))
    }.groupBy { case (key, _) => key }
      .mapValues {
        values =>
          values
            .map { case (_, metrics) => metrics }
            .reduce((metric1, metric2) => {
              metric1 + metric2
              metric1
            })
      }
      .toList
  }

  private def toPositionKey(trade: Trade) = Position(trade.security, trade.direction)

}

