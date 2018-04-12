import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object CrimeCountByMonthAndTypeDataFrame {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Crime Count by Month and Type using Data Frame"))
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val crimesRaw = sc.textFile("/user/cloudera/crimes.csv")

    val crimes = crimesRaw.
      filter(!_.startsWith("ID")).
      map(_.split(",")).
      map(ss => (ss(2).substring(6, 10) + ss(2).substring(0, 2), ss(5))).
      toDF("crime_month", "crime_type")

    crimes.registerTempTable("crimes")

    val crimeCountByMonthAndType = sqlContext.sql(
      s"""
       select crime_month, count(*) count, crime_type
       from crimes
       group by crime_month, crime_type
       order by crime_month, count desc""")

    val output = crimeCountByMonthAndType.rdd.
      map(_.mkString("\t")).
      coalesce(4)

    output.saveAsTextFile("/user/cloudera/solution01/crime_count_by_month_and_type_df",
      classOf[org.apache.hadoop.io.compress.GzipCodec])
  }
}
