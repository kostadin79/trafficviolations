//package traffic_violations
//
//import akka.actor.{ActorSystem, Props}
//import com.typesafe.config.ConfigFactory
//import scala.language.postfixOps
//
//object SendDataToDBApp extends App {
//  println("sendDataToDBApp started")
//  val sendDataToDBSystem = ActorSystem("sendDataToDBSystem", ConfigFactory.load("remote.conf").getConfig("sendDataToDBSystem"))
//  val sendDataToDBActor = sendDataToDBSystem.actorOf(Props[SendDataToDBActor.scala], "sendDataToDBActor")
//  sendDataToDBActor ! "start"
//}
