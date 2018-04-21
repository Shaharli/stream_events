import Main.{eventsStatsRoutes, system}
import akka.actor.ActorRef
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpRequest, StatusCodes}
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.testkit.ScalatestRouteTest

/**
  * Created by shaharavigezer on 21/04/2018.
  */
class RoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with EventsStatsRoutes {

  override val eventsStatsRef: ActorRef = system.actorOf(EventsStatsActor.props, "EventQueueActor")

  override def beforeAll(): Unit = {
    Http(system).bindAndHandle(eventsStatsRoutes, "localhost", 9000)
  }

  override def afterAll(): Unit = {
    system.terminate()
  }

  "Server Routes" should {

    "return a greeting for GET request to the root events path" in {

      Get("/events/") ~> eventsStatsRoutes ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] should ===("""Welcome!""")
      }
    }

    "return event type stats for GET request to the events/type path" in {

      Get("/events/type") ~> eventsStatsRoutes ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] should include("""Event type""")
      }
    }

    "return event data stats for GET request to the events/data path" in {

      Get("/events/data") ~> eventsStatsRoutes ~> check {
        status should ===(StatusCodes.OK)
        entityAs[String] should include("""Event data""")
      }
    }
  }

}
