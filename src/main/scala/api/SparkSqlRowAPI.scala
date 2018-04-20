val sc = new org.apache.spark.SparkContext(new org.apache.spark.SparkConf().setAppName("Spark shell"))
val sqlContext: org.apache.spark.sql.SQLContext = new org.apache.spark.sql.hive.HiveContext(sc)

import scala.Predef._
import org.apache.spark.SparkContext._
import sqlContext.implicits._
import sqlContext.sql
import org.apache.spark.sql.functions._

///////////WRITE CODE BELOW /////////////////////////

//Row used by dataframe map, flatMap functions

val orders = sqlContext.table("orders")
val row = orders.first()
row(0)
row(3)
row.getInt(0)
row.isNullAt(3)

row.anyNull
row.fieldIndex("order_id")
row.getAs[Int]("order_id")

row.size
row.length

row.schema
row.schema.printTreeString
row.toSeq

row.mkString
row.mkString(",")
row.mkString("<", ",", ">")