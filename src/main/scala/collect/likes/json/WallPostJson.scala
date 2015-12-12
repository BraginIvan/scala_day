package collect.likes.json

import spray.json.DefaultJsonProtocol

/**
 * Created by ivan on 11/24/15.
 */
case class WallPostJson(response: Array[Either[Long,WallPostBody]])

case class WallPostBody(id:Long, likes: Likes)

case class Likes(count: Int)

object JsonConverter extends DefaultJsonProtocol{
  implicit val likes = jsonFormat1(Likes)
  implicit val wallPostBody = jsonFormat2(WallPostBody)
  implicit val wallPostJson = jsonFormat1(WallPostJson)
}

