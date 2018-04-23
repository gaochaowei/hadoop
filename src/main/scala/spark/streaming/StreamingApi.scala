val sc = new org.apache.spark.SparkContext(new org.apache.spark.SparkConf().setAppName("Spark shell"))
val sqlContext: org.apache.spark.sql.SQLContext = new org.apache.spark.sql.hive.HiveContext(sc)

import scala.Predef._
import org.apache.spark.SparkContext._
import sqlContext.implicits._
import sqlContext.sql
import org.apache.spark.sql.functions._

///////////WRITE CODE BELOW /////////////////////////

import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream._

val ssc = new StreamingContext(sc, Seconds(1))

val s1: DStream[String] = ssc.textFileStream("/user/cloudera")
val s3: InputDStream[(String,String)] = ssc.fileStream("/user/cloudera/")

val s2: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
val words = s2.flatMap(_.split(" "))
val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
wordCounts.print

ssc.start
ssc.awaitTermination