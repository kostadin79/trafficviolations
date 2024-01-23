package send_external_system

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.language.postfixOps

object  SendNotificationToExternalSystemApp extends App {
  println("sendNotificationToExternalSystemApp started")
  val sendNotificationToExternalSystem = ActorSystem("SendNotificationToExternalSystem", ConfigFactory.load("remote.conf").getConfig("SendNotificationToExternalSystem"))
  val sendNotificationToExternalSystemActor = sendNotificationToExternalSystem.actorOf(Props[SendNotificationToExternalSystemActor], "sendNotificationToExternalSystemActor")
  sendNotificationToExternalSystemActor ! "test"
}
