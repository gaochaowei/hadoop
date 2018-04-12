import org.apache.spark.{SparkConf, SparkContext}

object CrimeCountByMonthAndTypeCoreAPI {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Crime Count by Month and Type using Core API"))

    val crimesRaw = sc.textFile("/user/cloudera/crimes.csv")

    val crimes = crimesRaw.
      filter(!_.startsWith("ID")).
      map(_.split(",")).
      map(ss => (ss(2).substring(6, 10) + ss(2).substring(0, 2), ss(5)))

    val crimeCountByMonthAndType = crimes.
      map(t => (t, 1)).
      reduceByKey(_ + _)

    val output = crimeCountByMonthAndType.
      sortBy(t => (t._1._1, -t._2)).
      map(t => List(t._1._1, t._2, t._1._2).mkString("\t")).
      coalesce(4)


    output.saveAsTextFile("/user/cloudera/solution01/crime_count_by_month_and_type",
      classOf[org.apache.hadoop.io.compress.GzipCodec])
  }
}
