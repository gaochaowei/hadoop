val sc = new org.apache.spark.SparkContext(new org.apache.spark.SparkConf().setAppName("Spark shell"))
val sqlContext: org.apache.spark.sql.SQLContext = new org.apache.spark.sql.hive.HiveContext(sc)

import scala.Predef._
import org.apache.spark.SparkContext._
import sqlContext.implicits._
import sqlContext.sql
import org.apache.spark.sql.functions._

///////////WRITE CODE BELOW /////////////////////////
val orders = sqlContext.table("orders")
val orderItems = sqlContext.table("order_items")
val products = sqlContext.table("products")

val orderId = $"order_id"
val orderDate = expr("order_date")
val orderCustomerId = orders.col("order_customer_id")
val orderStatus = $"order_status"
val orderItemSubtotal = $"order_item_subtotal"
val orderDateFormatted = expr("date_format(order_date,'dd/MM/yyyy') as order_date_formatted")

//SELECT
$"order_status".alias("status")
$"order_date".cast("date")
$"order_item_subtotal".divide(100)
-$"order_item_subtotal"
$"product_price" + 5.0
$"product_price" - 5.0
$"product_price" * 2
$"product_price" / 2
$"product_price" % 100
$"order_date".substr(6, 2).alias("month")
when($"order_status".isin("PAYMENT_REVIEW", "PENDING", "PENDING_PAYMENT", "PROCESSING"), 0)
  .when($"order_status".isin("CLOSED", "COMPLETE", "CANCELED"), 1)
  .otherwise(3)

//WHERE / FILTER
$"order_item_subtotal".between(100, 500)
!$"order_item_subtotal".between(100, 500)
$"order_status" === "PROCESSING" and $"order_date" startsWith "2017"
//===, !==, <=, >=, >, <, <=> (equalNullSafe)
$"order_date".isNull
$"order_date".isNotNull
$"order_status".isin("CLOSED", "COMPLETE", "CANCELED")
$"product_name".like("%Phone%")
$"product_name".rlike("%Phone%")

//ORDER BY
$"order_item_subtotal".asc
$"order_item_subtotal".desc

//OTHRS
orders("order_id").explain(true)