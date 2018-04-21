
import java.io.{BufferedReader, InputStream}

import EventsStreamInput.validate
import Main.eventsStatsRef
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import play.api.libs.json.{JsValue, Json}

import scala.util.Try


object Main extends App with EventsStatsRoutes with JsonSupport{

  implicit val system: ActorSystem = ActorSystem("Server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val eventsStatsRef: ActorRef = system.actorOf(EventsStatsActor.props, "EventQueueActor")

  Http(system).bindAndHandle(eventsStatsRoutes, "localhost", 9000)
  println(s"Server online at http://localhost:9000/")

  readLines(EventsStreamInput.startStream("src/generator-macosx-amd64"))

  def readLines(reader : BufferedReader): Unit = {
    var line = ""

    while ((line = reader.readLine()) != "") {
      val eventProcessOpt = Try(validate(Json.parse(line)))
      if (eventProcessOpt.isSuccess){
        eventsStatsRef ! Json.parse(line).as[EventInfo]
      }
    }
  }

}


