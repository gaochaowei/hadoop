import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Top3ResidentCrimeTypesDataFrame {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("Top 3 residential crime types using Core API"))
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val crimesRaw = sc.textFile("/user/cloudera/crimes.csv")

    val crimes = crimesRaw.
      filter(!_.startsWith("ID")).
      map(_.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")).
      filter(_ (7) == "RESIDENCE").
      map(_ (5)).toDF("crime_type")

    crimes.registerTempTable("crimes")

    val top3ResidentCrimeTypes = sqlContext.sql(
      s"""
         select crime_type, count(1) crime_count
         from crimes
         group by crime_type
         order by crime_count desc
         limit 3
       """
    ).coalesce(1)

    top3ResidentCrimeTypes.write.json("/user/cloudera/solution03/top_3_residence_crime_types_df")
  }
}
