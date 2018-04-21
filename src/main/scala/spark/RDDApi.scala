val sc = new org.apache.spark.SparkContext(new org.apache.spark.SparkConf().setAppName("Spark shell"))
val sqlContext: org.apache.spark.sql.SQLContext = new org.apache.spark.sql.hive.HiveContext(sc)

///////////WRITE CODE BELOW /////////////////////////

val rdd = sqlContext.table("sqoop_import_all.order_items").
  select("order_item_id", "order_item_order_id", "order_item_subtotal") rdd

val maxPriceByReduce = rdd.map(_.getDouble(2)).reduce(_.max(_))
val maxPriceByFolder = rdd.map(_.getDouble(2)).fold(0f)(_.max(_))
val maxPricaByAggregate = rdd.aggregate(0.0)((f, r) => f.max(r.getDouble(2)), _ max _)

rdd.filter(_.getDouble(2)>100)
rdd.groupBy(_.getDouble(2))
rdd.sortBy(_.getDouble(2),false)

rdd.take(3)
rdd.takeOrdered(3)
rdd.top(3)

