val sc = new org.apache.spark.SparkContext(new org.apache.spark.SparkConf().setAppName("Spark shell"))
val sqlContext: org.apache.spark.sql.SQLContext = new org.apache.spark.sql.hive.HiveContext(sc)

import scala.Predef._
import org.apache.spark.SparkContext._
import sqlContext.implicits._
import sqlContext.sql
import org.apache.spark.sql.functions._

///////////WRITE CODE BELOW /////////////////////////

val orderItems = sqlContext.table("order_items")
val groupedOrderItems = orderItems.groupBy("order_item_product_id")

groupedOrderItems.count
groupedOrderItems.min()
groupedOrderItems.max("order_item_subtotal")
groupedOrderItems.avg("order_item_subtotal", "order_item_quantity")
groupedOrderItems.mean("order_item_subtotal")
groupedOrderItems.sum("order_item_subtotal")

groupedOrderItems.agg(sum("order_item_subtotal").as("total"), max("order_item_quantity").as("avg_quantity"))
groupedOrderItems.agg("order_item_subtotal" -> "sum", "order_item_quantity" -> "max")
groupedOrderItems.agg(avg("order_item_subtotal"), count(lit(1)))

groupedOrderItems.pivot("order_item_quantity").sum("order_item_subtotal")
