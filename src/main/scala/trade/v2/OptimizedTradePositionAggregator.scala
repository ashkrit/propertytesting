package trade.v2

import trade.{Metrics, Position, Trade, TradeAggregator}

class OptimizedTradePositionAggregator(trades: Seq[Trade]) extends TradeAggregator {

  val metrics = calculate()

  @Override def noOfTrades(): Int = {
    metrics.map(_._2.count).sum
  }

  @Override def totalQty(): Long = {
    metrics.map(_._2.totalQty).sum
  }

  @Override def buyQty(): Long = {
    metrics.filter(_._1.direction == "BUY").map(_._2.totalQty).sum
  }

  @Override def sellQty(): Long = {
    metrics.filter(_._1.direction == "SELL").map(_._2.totalQty).sum
  }

  @Override def metricsBySecurity(): Seq[(String, String, Double)] = {

    metrics.map { case (key, metric) => (key.sec, key.direction, metric.tradeAmount) }
      .toList

  }

  def calculate(): Seq[(Position, Metrics)] = {
    trades.map { trade => (toPositionKey(trade), Metrics(1, trade.qty, trade.qty * trade.price))
    }.groupBy { case (key, _) => key }
      .mapValues {
        values =>
          values.map { case (_, metrics) => metrics }.reduceLeft((baseMetrics, trade) => {
            baseMetrics + trade
            baseMetrics
          })
      }
      .toList
  }

  private def toPositionKey(trade: Trade) = {
    Position(trade.security, trade.direction)
  }
}

