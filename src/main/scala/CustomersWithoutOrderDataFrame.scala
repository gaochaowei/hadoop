import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object CustomersWithoutOrderDataFrame {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Customers without order using Data Frame"))
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val customersRaw = sc.textFile("/user/cloudera/retail_db/customers")
    val ordersRaw = sc.textFile("/user/cloudera/retail_db/orders")

    val customers = customersRaw.
      map(_.split(",")).
      map(s => (s(0), s(1), s(2))).
      toDF("customer_id", "customer_lname", "customer_fname")
    val orders = ordersRaw.
      map(_.split(",")).
      map(_ (2)).
      distinct.
      map((_, 1)).
      toDF("customer_id", "one")

    customers.registerTempTable("customers")
    orders.registerTempTable("orders")

    val customersWithoutOrder = sqlContext.sql(
      s"""
         select customer_lname, customer_fname
         from customers
         left join orders on customers.customer_id=orders.customer_id
         where orders.one is null
         order by customer_lname, customer_fname
       """)

    val output = customersWithoutOrder.rdd.
      map(_.mkString(", ")).
      coalesce(1)

    output.saveAsTextFile("/user/cloudera/solution02/customers_without_order_df")
  }
}
