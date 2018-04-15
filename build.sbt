name := "hadoop"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "1.6.3"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.3"
libraryDependencies += "com.databricks" % "spark-avro_2.11" % "2.0.1"