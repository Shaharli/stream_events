import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
/**
  * Created by shaharavigezer on 21/04/2018.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val statsJsonFormat = jsonFormat2(EventsStats)
}
