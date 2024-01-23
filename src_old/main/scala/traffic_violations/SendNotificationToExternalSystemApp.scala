package traffic_violations

import traffic_violations.InputRequestsFromAPIActor._
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory

import scala.language.postfixOps
// step 1
import spray.json._

import scala.concurrent.duration._
object  SendNotificationToExternalSystemApp extends App {
  println("sendNotificationToExternalSystemApp started")
  val sendNotificationToExternalSystem = ActorSystem("SendNotificationToExternalSystem", ConfigFactory.load("remote.conf").getConfig("SendNotificationToExternalSystem"))
  val sendNotificationToExternalSystemActor = sendNotificationToExternalSystem.actorOf(Props[SendNotificationToExternalSystemActor], "sendNotificationToExternalSystemActor")
  sendNotificationToExternalSystemActor ! "test"
}
