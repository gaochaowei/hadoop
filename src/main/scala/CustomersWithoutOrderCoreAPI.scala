import org.apache.spark.{SparkConf, SparkContext}

object CustomersWithoutOrderCoreAPI {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Customers without order using Core API"))

    val customersRaw = sc.textFile("/user/cloudera/retail_db/customers")
    val ordersRaw = sc.textFile("/user/cloudera/retail_db/orders")

    val customers = customersRaw.
      map(_.split(",")).
      map(s => (s(0), s(1), s(2)))
    val orders = ordersRaw.
      map(_.split(",")).
      map(s => s(2)).
      distinct

    val customersById = customers.
      map(s => (s._1, (s._2, s._3)))
    val ordersByCustomerId = orders.
      map(s => (s, "1"))
    val customersWithoutOrder = customersById.
      leftOuterJoin(ordersByCustomerId).
      filter(s => s._2._2.isEmpty).
      map(s => s._2._1).
      sortByKey().
      map(_.productIterator.mkString(", ")).
      coalesce(1)

    customersWithoutOrder.saveAsTextFile("/user/cloudera/solution02/customers_without_order")
  }
}
