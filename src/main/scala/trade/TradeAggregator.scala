package trade

trait TradeAggregator {
  def noOfTrades(): Int

  def totalQty(): Long

  def buyQty(): Long

  def sellQty(): Long

  def metricsBySecurity(): Seq[(String, String, Double)]
}
