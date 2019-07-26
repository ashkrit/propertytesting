package trade

class TradePositionAggregator(trades: Seq[Trade]) {

  def noOfTrades(): Int = {
    trades.size
  }

  def totalQty(): Long = {
    trades.map(_.qty).sum
  }

  def buyQty(): Long = {
    trades.filter(_.direction == "BUY").map(_.qty).sum
  }

  def sellQty(): Long = {
    trades.filter(_.direction == "SELL").map(_.qty).sum
  }


  def metricsBySecurity(): Seq[(String, String, Double)] = {

    trades
      .map { trade => ((trade.security, trade.direction), trade.qty * trade.price) }
      .groupBy { case (key, _) => key }
      .mapValues { values => values.map(_._2).sum }
      .map { case ((security, direction), value) => (security, direction, value) }
      .toList

  }
}

