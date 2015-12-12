package collect.likes.json

import spray.json.DefaultJsonProtocol


/**
 * Created by ivan on 11/26/15.
 */
case class PostLikesJson(response: Response)

case class Response(users: Array[Long])

object LikesJson extends DefaultJsonProtocol{
  implicit val response = jsonFormat1(Response)
  implicit val postLikes = jsonFormat1(PostLikesJson)

}