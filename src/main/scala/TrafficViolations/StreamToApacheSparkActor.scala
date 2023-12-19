package TrafficViolations

import akka.actor.{Actor, ActorLogging}


object StreamToApacheSparkActor {
  case class PrintMessage(message: String)
}

class StreamToApacheSparkActor extends Actor with ActorLogging {

  import StreamToApacheSparkActor._

  override def receive: Receive = {
    case message: String => log.info(message)
    case PrintMessage(message) => log.info(message)
  }

}
