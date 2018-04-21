
import EventsStatsActor.{GetEventsDataStats, GetEventsTypeStats}
import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by shaharavigezer on 21/04/2018.
  */

trait EventsStatsRoutes extends JsonSupport {

  implicit def system: ActorSystem

  def eventsStatsRef: ActorRef

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(5.seconds)

  lazy val log = Logging(system, classOf[EventsStatsActor])

  lazy val eventsStatsRoutes: Route = {
    pathPrefix("events") {
      concat(
        path(""){
          get {
            complete("Welcome!")
          }
        },
        pathPrefix("type") {
          get { ctx =>
            val typeStats = (eventsStatsRef ? GetEventsTypeStats)
              .mapTo[EventsStats]

            ctx.complete(typeStats)
          }
        },
        pathPrefix("data") {
          get { ctx =>
            val dataStats = (eventsStatsRef ? GetEventsDataStats)
              .mapTo[EventsStats]

            ctx.complete(dataStats)
          }
        }
      )
    }
  }
}
