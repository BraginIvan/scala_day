package analyze

import collect.likes.CollectVkGroupsLikes
import com.datastax.spark.connector._
import tools.{Config, SparkConfig}

/**
 * Created by ivan on 12/1/15.
 */
object TurnOutLikes extends SparkConfig{


  def analyze(): Unit = {

   spark.cassandraTable(Config.keyspace, "likes").map{ row =>
      val group = row.get[Long]("group_id")
      val post = row.get[Long]("post_id")
      val user = row.get[Long]("user_id")
      user -> List((group, post))
    }.reduceByKey(_ ::: _).map{
      userLikes =>
        val likesByGroup: Map[Long, Int] = userLikes._2.map(_._1).groupBy(g => g).map(g => g._1 -> g._2.size)
        (
          userLikes._1,
          likesByGroup.getOrElse(CollectVkGroupsLikes.Security.groupId, 0),
          likesByGroup.getOrElse(CollectVkGroupsLikes.Dataart.groupId, 0)
          )
   }.saveToCassandra(Config.keyspace, "user_data", SomeColumns("user_id", "security_count", "dataart_count"))

    spark.stop()

  }
}

