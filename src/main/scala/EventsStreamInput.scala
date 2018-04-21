import java.io.{BufferedReader, InputStream, InputStreamReader}

import Main.system
import akka.actor.ActorRef
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.util.Try

/**
  * Created by shaharavigezer on 21/04/2018.
  */

case class EventsStreamInput()

object EventsStreamInput {

  def validate(jsValue: JsValue) = myReads.reads(jsValue)

  private val myReads: Reads[JsValue] = {
    (__ \ "event_type").read[String] ~>
      (__ \ 'data).read[String] ~>
      implicitly[Reads[JsValue]]
  }

  def startStream(inputPath: String) = {

    val process = new ProcessBuilder(inputPath).start()
    val processInputStream = process.getInputStream

    val reader: BufferedReader = new BufferedReader(
      new InputStreamReader(processInputStream))

    reader
  }
}
