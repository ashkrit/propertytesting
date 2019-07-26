package trade

case class Trade(customerId: Int, tradeId: String, security: String, direction: String, qty: Int, price: Double)
