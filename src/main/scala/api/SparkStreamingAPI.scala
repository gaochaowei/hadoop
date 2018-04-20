val sc = new org.apache.spark.SparkContext(new org.apache.spark.SparkConf().setAppName("Spark shell"))
val sqlContext: org.apache.spark.sql.SQLContext = new org.apache.spark.sql.hive.HiveContext(sc)

import scala.Predef._
import org.apache.spark.SparkContext._
import sqlContext.implicits._
import sqlContext.sql
import org.apache.spark.sql.functions._

///////////WRITE CODE BELOW /////////////////////////

import org.apache.spark.streaming._

val ssc = new StreamingContext(sc, Seconds(1))

ssc.textFileStream("/user/cloudera/*log")
ssc.socketTextStream("localhost", 9999)

ssc.start()
ssc.awaitTermination()