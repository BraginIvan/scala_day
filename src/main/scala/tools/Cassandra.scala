package tools

import collect.likes.LikeData
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.querybuilder.QueryBuilder

/**
 * Created by ivan on 12/1/15.
 */
trait Cassandra {
  /*

  CREATE KEYSPACE scala_day WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = false;

  CREATE TABLE scala_day.likes (
    group_id bigint,
    post_id bigint,
    user_id bigint,
    PRIMARY KEY (group_id, post_id, user_id)
    );

   CREATE TABLE scala_day.user_data (
    user_id bigint PRIMARY KEY,
    security_count int,
    dataart_count int,
    last_seen bigint,
    albums int,
    audios int,
    followers int,
    photos int,
    videos int,
    isMale int,
    can_write_private_message int
    );

  */
  val session = Cluster.builder().addContactPoint("localhost").build().connect(Config.keyspace)

  def storeLikeData(groupId:Long,postId:Long,userId:Long): Unit ={
    session.execute(
      QueryBuilder.insertInto("likes")
        .value("group_id", groupId)
        .value("post_id", postId)
        .value("user_id", userId)
    )
  }
}
