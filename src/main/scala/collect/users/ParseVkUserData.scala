package collect.users

import collect.users.json.{UserDataJson,UserData}
import UserDataJson._
import spray.json._
import tools.{Config, SparkConfig, HttpConnector, Cassandra}
import scala.collection.JavaConversions._
import com.datastax.spark.connector._

/**
 * Created by ivan on 12/1/15.
 */
object ParseVkUserData extends SparkConfig with HttpConnector{

  def collect(): Unit = {
    val users = spark.cassandraTable(Config.keyspace, "user_data")
      .filter(_.isNullAt("can_write_private_message"))
      .map(_.getLong("user_id").toString).flatMap(parseUsers)
    .saveToCassandra(Config.keyspace, "user_data", SomeColumns("user_id", "albums","audios", "followers", "photos", "videos", "last_seen", "ismale", "can_write_private_message"))

  }

  def parseUsers(users:String) ={
    getContent(s"""https://api.vk.com/method/users.get?user_ids=$users&fields=counters,last_seen,sex,bdate,can_write_private_message""").parseJson.convertTo[UserData]
      .response.map{
      response =>
        (
          response.uid,
          response.counters.map(_.albums).flatten,
          response.counters.map(_.audios).flatten,
          response.counters.map(_.followers).flatten,
          response.counters.map(_.photos).flatten,
          response.counters.map(_.videos).flatten,
          response.last_seen.map(_.time),
          if(response.sex.getOrElse(1) == 2) 1 else 0,
          response.can_write_private_message
        )
    }
  }

}
