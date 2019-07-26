package trading

import org.h2.jdbcx.JdbcConnectionPool
import trade.Trade

import scala.collection.mutable.ArrayBuffer

class AggregationExpectedResult(trades: Seq[Trade], tableName: String) {

  val cp = JdbcConnectionPool.create("jdbc:h2:~/test", "sa", "sa")
  val conn = cp.getConnection

  conn.createStatement().execute(s"drop table ${tableName}")
  conn.createStatement().execute(
    s"""
       |create table ${tableName}(
       |customerid int,
       |tradeid varchar(100),
       |security varchar(10),
       |direction varchar(10) ,
       |qty int,
       |price double(32)
       | )
       | """.stripMargin)


  trades.foreach(trade => {
    val sql =
      s"""
         |INSERT INTO trade(customerid, tradeid, security,direction,qty,price)
         |values(
         |${trade.customerId},'${trade.tradeId}', '${trade.security}', '${trade.direction}' , ${trade.qty} , ${trade.price}
         |)
      """.stripMargin
    conn.createStatement().execute(sql)
  })

  def noOfTrades(): Int = {
    val rs = conn.createStatement().executeQuery("SELECT count(1) from trade")
    rs.next()
    rs.getInt(1)
  }

  def totalQty(): Long = {
    val rs = conn.createStatement().executeQuery("SELECT sum(qty) from trade")
    rs.next()
    rs.getInt(1)
  }

  def buyQty(): Long = {
    val rs = conn.createStatement().executeQuery("SELECT sum(qty) from trade WHERE direction='BUY'")
    rs.next()
    rs.getLong(1)

  }

  def sellQty(): Long = {
    val rs = conn.createStatement().executeQuery("SELECT sum(qty) from trade WHERE direction='SELL'")
    rs.next()
    rs.getLong(1)
  }

  def metricsBySecurity(): Seq[(String, String, Double)] = {
    val rs = conn.createStatement().executeQuery(
      """
        |SELECT
        |security, direction, sum(qty*price)
        |from
        |trade
        |GROUP BY security, direction
      """.stripMargin)

    val result = new ArrayBuffer[(String, String, Double)]()
    while (rs.next()) {
      val sec = rs.getString(1)
      val direction = rs.getString(2)
      val totalValue = rs.getDouble(3)
      result.append((sec, direction, totalValue))
    }

    result.toList
  }

}
