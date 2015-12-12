package tools

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by ivan on 12/1/15.
 */
trait SparkConfig {

  private val sparkConfig = new SparkConf(true).setAppName(getClass.getSimpleName)
    .setMaster(s"local[1]")
    .set("spark.executor.memory", "2g")
    .set("spark.cassandra.connection.host", "localhost")

  val spark = new SparkContext(sparkConfig)
}
