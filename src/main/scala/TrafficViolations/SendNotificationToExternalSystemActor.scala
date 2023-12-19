package TrafficViolations

import akka.actor.{Actor, ActorLogging}

object SendNotificationToExternalSystemActor {
  case class PrintMessage(message: String)
}

class SendNotificationToExternalSystemActor extends Actor with ActorLogging{
  override def receive: Receive = {
    case message: String => log.info(s"$message")
  }
}
