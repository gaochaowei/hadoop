import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

val sc = new SparkContext(new SparkConf().setAppName("050-Spark Core"))
val ssc = new StreamingContext(sc, Seconds(1))

ssc.textFileStream("/user/cloudera/*log")
ssc.socketTextStream("localhost",9999)

ssc.start()
ssc.awaitTermination()