package send_db

import akka.actor.{Actor, ActorLogging}
object SendDataToDBActor {
  case class SendMessageToDB(message: String)
}

class SendDataToDBActor extends Actor with ActorLogging{
  import SendDataToDBActor._
  override def receive: Receive = {
    case message: String => log.info(s"$message")
    case SendMessageToDB(message) => log.info(s"$message")
  }
}


