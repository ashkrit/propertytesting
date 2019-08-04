package trade

case class Trade(customerId: Int, tradeId: String, security: String, direction: String, qty: Int, price: Double)

case class Position(sec: String, direction: String)

case class Metrics(var count: Int = 0, var totalQty: Long = 0, var tradeAmount: Double = 0) {
  def +(m: Metrics): Unit = {
    this.count += m.count
    this.totalQty += m.totalQty
    this.tradeAmount += m.tradeAmount
  }
}