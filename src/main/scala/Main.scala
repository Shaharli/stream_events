
import java.io.InputStream
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import play.api.libs.json.{JsValue, Json}


object Main extends App with EventsStatsRoutes with JsonSupport{

  implicit val system: ActorSystem = ActorSystem("Server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val eventsStatsRef: ActorRef = system.actorOf(EventsStatsActor.props, "EventQueueActor")

  // starting server
  Http(system).bindAndHandle(eventsStatsRoutes, "localhost", 9000)
  println(s"Server online at http://localhost:9000/")

  // get input stream from file
  val process: Process = new ProcessBuilder("src/generator-macosx-amd64").start
  val processInputStream: InputStream = process.getInputStream

  eventsStatsRef ! Json.parse(processInputStream).as[EventInfo]

}
