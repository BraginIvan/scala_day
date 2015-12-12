package collect.likes

import collect.likes.json.{LikesJson, PostLikesJson, JsonConverter, WallPostJson}
import spray.json._
import LikesJson._
import JsonConverter._
import tools.{HttpConnector, Cassandra}


case class LikeData(groupId: Long, postId: Long, userId:Long)
case class PostData(postId: Long, likesCount: Int)


object CollectVkGroupsLikes extends HttpConnector with Cassandra{

  case class GroupToParsing(groupId:Long, postCount:Int)
  val Security = GroupToParsing(777107l, 12)
  val Dataart = GroupToParsing(24458631l, 500)

  val groups = List(Security, Dataart)

  def collect() = {
    extractGroupsLikes()
    httpClient.close()
  }
    
  def extractGroupsLikes() = groups.foreach(extractGroupLikes)

  def extractGroupLikes(group: GroupToParsing) =
    postsRequests(group)
    .map(getContent)
    .flatMap(parseGroupResponse)
    .map(response => PostData(response.id, response.likes.count))
    .foreach(extractPostLikes(group.groupId, _))

  def extractPostLikes(group: Long, postData: PostData) =
    likesRequests(group, postData)
      .map(getContent)
      .foreach(parsePostResponse(_).foreach(storeLikeData(group, postData.postId, _)))

  
  def postsRequest(groupId:Long, offset:Int, count: Int = 100) =
    count match {
      case 0 => None
      case _ => Some(s"https://api.vk.com/method/wall.get?owner_id=-$groupId&count=$count&offset=$offset")
    }

  def likesRequest(groupId:Long, postId: Long, offset:Int) =
    s"https://api.vk.com/method/likes.getList?type=post&owner_id=-$groupId&item_id=$postId&filter&likes&count=1000&offset=$offset"


  def postsRequests(group: GroupToParsing): List[String] = {
    val postsFromGroup =  group.postCount
    (
      postsRequest(group.groupId, postsFromGroup - (postsFromGroup % 100), postsFromGroup % 100) ::
        (0 until (postsFromGroup / 100)).map(_ * 100).toList
          .map(n => postsRequest(group.groupId, n))
      ).filter(_.nonEmpty).map(_.get)
  }

  def likesRequests(groupId: Long, postData:PostData) =
    (0 to (postData.likesCount / 1000)).map(_ * 1000)
    .map(likesRequest(groupId, postData.postId, _))

  def parseGroupResponse(response:String) = {
    response
      .parseJson.convertTo[WallPostJson].response
      .collect { case Right(x) => x }
  }

  def parsePostResponse(response:String) = response.parseJson.convertTo[PostLikesJson].response.users

}
