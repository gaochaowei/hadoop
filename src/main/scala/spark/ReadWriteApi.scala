val sc = new org.apache.spark.SparkContext(new org.apache.spark.SparkConf().setAppName("Spark shell"))
val sqlContext: org.apache.spark.sql.SQLContext = new org.apache.spark.sql.hive.HiveContext(sc)

import scala.Predef._
import org.apache.spark.SparkContext._
import sqlContext.implicits._
import sqlContext.sql
import org.apache.spark.sql.functions._

///////////WRITE CODE BELOW /////////////////////////

import com.databricks.spark.avro._

import org.apache.hadoop.io.compress.SnappyCodec
import org.apache.hadoop.io.compress.GzipCodec
import org.apache.hadoop.io.compress.DefaultCodec
import org.apache.hadoop.io.compress.DeflateCodec
import org.apache.hadoop.io.compress.BZip2Codec
import org.apache.hadoop.io.compress.Lz4Codec

//RDD
val rdd = sc.textFile("/user/cloudera/retail_db/orders").coalesce(1)
sc.sequenceFile("/user/cloudera/sqoop_import_seqfile/orders")
sc.sequenceFile("/user/cloudera/sqoop_import_seqfile/orders", classOf[String], classOf[Int])
sc.wholeTextFiles("/user/cloudera/sqoop_import_text_snappy/oders")

rdd.saveAsTextFile("/user/cloudera/spark_rdd_save_as_text/orders")
rdd.saveAsTextFile("/user/cloudera/spark_rdd_save_as_text/orders_snappy", classOf[SnappyCodec])
rdd.saveAsTextFile("/user/cloudera/spark_rdd_save_as_text/orders_gzip", classOf[GzipCodec])
rdd.saveAsTextFile("/user/cloudera/spark_rdd_save_as_text/orders_default", classOf[DefaultCodec])
rdd.saveAsTextFile("/user/cloudera/spark_rdd_save_as_text/orders_deflate", classOf[DeflateCodec])
rdd.saveAsTextFile("/user/cloudera/spark_rdd_save_as_text/orders_bzip2", classOf[BZip2Codec])
rdd.saveAsTextFile("/user/cloudera/spark_rdd_save_as_text/orders_lz4", classOf[Lz4Codec])

//DataFrame

val df = sqlContext.read.table("orders").coalesce(1)
sqlContext.read.format("json").option("path", "/user/cloudera/retail_db/orders").load().coalesce(1)
sqlContext.sql("select * from orders")
sqlContext.read.json("/user/cloudera/spark_write/orders_json")
sqlContext.read.text("/user/cloudera/spark_write/orders_text")
sqlContext.read.parquet("/user/cloudera/sqoop_import_hdfs/orders_parquet/")
sqlContext.read.orc("/user/cloudera/spark_write/orders_orc")
sqlContext.read.avro("/user/cloudera/spark_write/orders_avro")

val prop = new java.util.Properties
prop.setProperty("user", "retail_dba")
prop.setProperty("password", "cloudera")
sqlContext.read.jdbc("jdbc:004-Mysql://quickstart:3306/retail_db", "orders_spark_jdbc", prop)

df.toJSON.saveAsTextFile("/user/cloudera/spark_write/orders_json")
df.write.format("json").mode("append").option("path", "/user/cloudera/spark_write/orders_json").save()
df.write.mode("append").json("/user/cloudera/spark_write/orders_json")
//mode: overwrite|append|ignore|error

sqlContext.setConf("spark.hadoop.mapred.output.compress", "true")
sqlContext.setConf("spark.hadoop.mapred.output.compression.codec", "org.apache.hadoop.io.compress.SnappyCodec")
sqlContext.setConf("spark.hadoop.mapred.output.compression.type", "BLOCK")
df.write.json("/user/cloudera/spark_write/orders_json_snappy") //UNCOMPRESSED
sqlContext.setConf("spark.hadoop.mapred.output.compress", "false")

sqlContext.getConf("spark.sql.parquet.compression.codec") //gzip
sqlContext.setConf("spark.sql.parquet.compression.codec", "uncompressed")
df.write.parquet("/user/cloudera/spark_write/orders_parquet")
sqlContext.setConf("spark.sql.parquet.compression.codec", "snappy")
df.write.parquet("/user/cloudera/spark_write/orders_parquet_snappy")
sqlContext.setConf("spark.sql.parquet.compression.codec", "gzip")
df.write.parquet("/user/cloudera/spark_write/orders_parquet_gzip")
sqlContext.setConf("spark.sql.parquet.compression.codec", "uncompressed")

df.write.orc("/user/cloudera/spark_write/orders_orc")
???

sqlContext.getConf("spark.sql.parquet.compression.codec") //gzip
sqlContext.setConf("spark.sql.avro.compression.codec", "uncompressed")
df.write.avro("/user/cloudera/spark_write/orders_avro")
sqlContext.setConf("spark.sql.avro.compression.codec", "snappy")
df.write.avro("/user/cloudera/spark_write/orders_avro_snappy")
sqlContext.setConf("spark.sql.avro.compression.codec", "uncompressed")

df.write.saveAsTable("orders_spark_save_as")
//hadoop fs -mkdir /user/cloudera/spark_write/orders_spark_create_externa
//sqlContext.createExternalTable("orders_spark_create_external", "/user/cloudera/spark_write/orders_spark_create_external")
//create table orders_new like orders
df.write.insertInto("orders_new")
df.write.mode("overwrite").jdbc("jdbc:004-Mysql://quickstart:3306/retail_db", "orders_spark_jdbc", prop)

df.toJSON.saveAsTextFile("/user/cloudera/spark_write/orders_text_json")
//Conversion
df.toJSON
df.rdd

import sqlContext.implicits._

rdd.toDF()
rdd.toDF("order_id", "order_date", "order_customer_id", "order_status")
