import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Top3ResidentCrimeTypesCoreAPI {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Top 3 residential crime types using Core API"))
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val crimesRaw = sc.textFile("/user/cloudera/crimes.csv")

    val crimes = crimesRaw.
      filter(!_.startsWith("ID")).
      map(_.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")).
      filter(_ (7) == "RESIDENCE").
      map(_ (5))

    val top3ResidentCrimeTypes = crimes.
      map((_, 1)).
      reduceByKey(_ + _).
      sortBy(_._2, false).
      take(3)

    val output = sc.parallelize(top3ResidentCrimeTypes).
      toDF("crime_type", "crime_count").
      coalesce(1)

    output.write.json("/user/cloudera/solution03/top_3_residence_crime_types")
  }
}
