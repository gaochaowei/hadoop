import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
import scala.Predef._
import org.apache.spark.SparkContext._

val sc = new SparkContext(new SparkConf().setAppName("050-Spark Read"))
val sqlContext = new SQLContext(sc)

import sqlContext.implicits._
import sqlContext.sql

sql("select * from orders")
//orders (order_id int, order_date datetime, order_customer_id int, order_status varchar2)
//order_items (order_item_id int, order_item_order_id int, order_item_product_id int, order_item_quantity int, order_item_subtotal float, order_item_product_price float)
val orders = sqlContext.table("orders")
val orderItems = sqlContext.table("order_items")

orders.select("order_id","order_date")
orders.select($"order_date")
orders.selectExpr("from_unixtime(order_date/1000,'yyyy/MM/dd') as order_date")
orders.drop("order_customer_id")

orders.distinct
orders.dropDuplicates
orders.dropDuplicates(List("order_date","order_customer_id"))

orders.collect
orders.columns
orders.count

orders.cube("order_date","order_customer_id")
orders.cube("order_date","order_customer_id").count.show //CONTAINS NULL DATE
orders.groupBy("order_date","order_customer_id")
orders.groupBy("order_date","order_customer_id").count.show

orders.describe("order_id","order_customer_id")

orders.dtypes
orders.schema
orders.printSchema

orders.explain

orderItems.filter("order_item_quantity > 1")
orderItems.where("order_item_quantity > 1")


orders.isLocal //FALSE

orders.first
orders.head
orders.head(10)
orders.take(10)
orders.limit(10)

//orders.join(orderItems,"order_id")
orders.join(orderItems,orders("order_id")===orderItems("order_item_order_id"))
orders.join(orderItems,orders("order_id")===orderItems("order_item_order_id"),"left_outer")
//orders.join(orderItems,List("order_id"),"left_outer") //One of: `inner`, `outer`, `left_outer`, `right_outer`, `leftsemi`.

orders.sort("order_customer_id")
orders.sort(orders("order_customer_id").desc)
orders.orderBy("order_customer_id")
orders.orderBy(orders("order_customer_id").desc)

orders.except(orders)
orders.intersect(orders)
orders.unionAll(orders)

orders.toJSON


