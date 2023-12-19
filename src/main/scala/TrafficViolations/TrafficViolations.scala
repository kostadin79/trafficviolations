package TrafficViolations


import TrafficViolations.InputRequestsFromAPIActor._
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
// step 1
import spray.json._

import scala.concurrent.duration._

case class Violation(violationId: String, violationClass: String, level: Int)


trait ViolationJsonProtocol extends DefaultJsonProtocol {
      implicit val violationFormat: RootJsonFormat[Violation] = jsonFormat3(Violation)
}

object TrafficViolationsApp extends App with ViolationJsonProtocol with SprayJsonSupport {

      implicit val system: ActorSystem = ActorSystem("ViolationSystem")
      implicit val materializer: ActorMaterializer = ActorMaterializer()
      import TrafficViolations.{InputRequestsFromAPIActor, StreamToApacheSparkActor}
      import system.dispatcher

////      val sendDataToDBActor = system.actorOf(Props[SendDataToDBActor], "sendDataToDBActor")
//    val sendDataToDBSystem = ActorSystem("sendDataToDBActor", ConfigFactory.load("remote.conf").getConfig("remoteSystem"))

//  private val config = ConfigFactory.load("remote.conf")
//  private val sendDataToDBIP = config.getString("ip-mapping.ip-db-actor")
//  private val sendNotificationToExternalSystemIP = config.getString("ip-mapping.ip-external-system-actor")
//  val sendDataToDBActor = system.actorSelection(s"akka://sendDataToDBSystem@${sendDataToDBIP}:2552/user/sendDataToDBActor")
//  val sendNotificationToExternalSystemActor = system.actorSelection(s"akka://SendNotificationToExternalSystem@${sendNotificationToExternalSystemIP}:2553/user/sendNotificationToExternalSystemActor")
val sendDataToDBSystem = ActorSystem("sendDataToDBSystem", ConfigFactory.load("remote.conf").getConfig("sendDataToDBSystem"))
  val sendDataToDBActor = sendDataToDBSystem.actorOf(Props[SendDataToDBActor], "sendDataToDBActor")
  val sendNotificationToExternalSystem = ActorSystem("SendNotificationToExternalSystem", ConfigFactory.load("remote.conf").getConfig("SendNotificationToExternalSystem"))
  val sendNotificationToExternalSystemActor = sendNotificationToExternalSystem.actorOf(Props[SendNotificationToExternalSystemActor], "sendNotificationToExternalSystemActor")
 val inputRequestsFromAPIActor = system.actorOf(Props( new InputRequestsFromAPIActor(sendDataToDBActor ,sendNotificationToExternalSystemActor)), "inputRequestsFromAPIActor")


      sendDataToDBActor ! "TEST sendDataToDBActor message"
      sendNotificationToExternalSystemActor ! "TEST sendDataToDBActor message"


      val initialData = List(
            Violation("XDdf65hg" , "SpeedViolationOnRoad", 70),
            Violation("JJYBdt4h" , "SpeedViolationOnCrossRoad", 67),
            Violation("YYhuj85k", "RedLightCrossRoadTown", 30)
      )

      initialData.foreach { violation =>
        println("actor start")
        inputRequestsFromAPIActor ! AddViolation(violation)
      }

      /*
        - GET /api/violation, returns all the players in the map, as JSON
        - GET /api/violation/(violationId), returns the player with the given nickname (as JSON)
        - GET /api/violation?violationId=X, does the same
        - GET /api/violation/class/(charClass), returns all the violations with the given character class
        - POST /api/violation with JSON payload, adds the player to the map
        - (Exercise) DELETE /api/violation with JSON payload, removes the player from the map
       */

      implicit val timeout = Timeout(2 seconds)
      val violationsRouteSkel =
            pathPrefix("api" / "violation") {
                  get {
                        path("class" / Segment) { characterClass =>
                              val playersByClassFuture = (inputRequestsFromAPIActor ? GetViolationsByClass(characterClass)).mapTo[List[Violation]]
                              complete(playersByClassFuture)

                        } ~
                          (path(Segment) | parameter('violationId)) { nickname =>
                                val playerOptionFuture = (inputRequestsFromAPIActor ? GetViolation(nickname)).mapTo[Option[Violation]]
                                complete(playerOptionFuture)
                          } ~
                          pathEndOrSingleSlash {
                                complete((inputRequestsFromAPIActor ? GetAllViolations).mapTo[List[Violation]])
                          }
                  } ~
                    post {
                          entity(implicitly[FromRequestUnmarshaller[Violation]]) { player =>
                                complete((inputRequestsFromAPIActor ? AddViolation(player)).map(_ => StatusCodes.OK))
                          }
                    } ~
                    delete {
                          entity(as[Violation]) { player =>
                                complete((inputRequestsFromAPIActor ? RemoveViolation(player)).map(_ => StatusCodes.OK))
                          }
                    }
            }

      Http().bindAndHandle(violationsRouteSkel, "localhost", 8080)

}

//object SendDataToDBApp extends App {
//  val sendDataToDBSystem = ActorSystem("sendDataToDBSystem", ConfigFactory.load("remote.conf").getConfig("sendDataToDBSystem"))
//  val sendDataToDBActor = sendDataToDBSystem.actorOf(Props[SendDataToDBActor], "sendDataToDBActor")
//}

//object  SendNotificationToExternalSystemApp extends App {
//  val sendNotificationToExternalSystem = ActorSystem("SendNotificationToExternalSystem", ConfigFactory.load("remote.conf").getConfig("SendNotificationToExternalSystem"))
//  val sendNotificationToExternalSystemActor = sendNotificationToExternalSystem.actorOf(Props[SendNotificationToExternalSystemActor], "sendNotificationToExternalSystemActor")
//}
