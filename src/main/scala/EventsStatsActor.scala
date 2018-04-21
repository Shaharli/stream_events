import akka.actor.{Actor, ActorLogging, Props}
import play.api.libs.json.Json

import scala.collection.mutable

/**
  * Created by shaharavigezer on 19/04/2018.
  */

final case class EventInfo(event_type: String, data: String)

final case class EventsStats(name: String, map: Map[String, Int])

object EventInfo {
  implicit val eventInfoFormat = Json.format[EventInfo]
}

object EventsStatsActor {

  final case object GetEventsDataStats

  final case object GetEventsTypeStats

  def props: Props = Props[EventsStatsActor]

}

class EventsStatsActor extends Actor with ActorLogging {

  import EventsStatsActor._

  var dataCount: mutable.Map[String, Int] = mutable.Map().withDefaultValue(0)
  var typeCount: mutable.Map[String, Int] = mutable.Map().withDefaultValue(0)

  override def receive: Receive = {

    case EventInfo(eventType, data) => {
      typeCount.update(eventType, typeCount(eventType) + 1)
      dataCount.update(data, dataCount(data) + 1)
    }

    case GetEventsDataStats =>
      sender() ! EventsStats("Event data", dataCount.toMap)

    case GetEventsTypeStats =>
      sender() ! EventsStats("Event type", typeCount.toMap)
  }
}
