package collect.users.json


import spray.json.DefaultJsonProtocol


case class UserData(response: List[Response])

case class Response(
                     uid: Long,
                     sex: Option[Int],
                     can_write_private_message: Int,
                     last_seen: Option[LastSeen],
                     counters: Option[Counters]
                     )

case class Counters(
                     albums:Option[Int],
                     videos:Option[Int],
                     audios:Option[Int],
                     photos:Option[Int],
                     followers:Option[Int]
                     )

case class LastSeen(time: Long)

object UserDataJson extends DefaultJsonProtocol{
  implicit val lastSeen = jsonFormat1(LastSeen)
  implicit val counters = jsonFormat5(Counters)
  implicit val response = jsonFormat5(Response)
  implicit val userData = jsonFormat1(UserData)

}