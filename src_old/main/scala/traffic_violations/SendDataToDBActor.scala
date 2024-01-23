//package traffic_violations
//
//import akka.actor.{Actor, ActorLogging}
//object SendDataToDBActor.scala {
//  case class SendMessageToDB(message: String)
//}
//
//class SendDataToDBActor.scala extends Actor with ActorLogging{
//  import SendDataToDBActor.scala._
//  override def receive: Receive = {
//    case message: String => log.info(s"$message")
//    case SendMessageToDB(message) => log.info(s"$message")
//  }
//}
//
//
