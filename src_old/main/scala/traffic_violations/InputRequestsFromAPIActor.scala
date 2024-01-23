package traffic_violations

import akka.actor.{Actor, ActorLogging, ActorSelection}

object InputRequestsFromAPIActor {
  case object GetAllViolations

  case class GetViolation(nickname: String)

  case class GetViolationsByClass(characterClass: String)

  case class AddViolation(player: Violation)

  case class RemoveViolation(player: Violation)

  case object OperationSuccess
}


class InputRequestsFromAPIActor(val sendDataToDB: ActorSelection, val sendNotification: ActorSelection) extends Actor with ActorLogging {

  import InputRequestsFromAPIActor._

  var violations: Map[String, Violation] = Map[String, Violation]()

  override def receive: Receive = {
    case GetAllViolations =>
      log.info("Getting all players")
      sender() ! violations.values.toList

    case GetViolation(nickname) =>
      log.info(s"Getting player with nickname $nickname")
      sender() ! violations.get(nickname)

    case GetViolationsByClass(characterClass) =>
      log.info(s"Getting all players with the character class $characterClass")
      sender() ! violations.values.toList.filter(_.violationClass == characterClass)

    case AddViolation(violation) =>
      log.info(s"Trying to add violation $violation")
      violations = violations + (violation.violationId -> violation)
      sendDataToDB ! s"violation: ${violation.violationId}"
      sendNotification ! s"violation: ${violation.violationId}"
    //      sender() ! OperationSuccess

    case RemoveViolation(violation) =>
      log.info(s"Trying to remove $violation")
      violations = violations - violation.violationId
      sender() ! OperationSuccess

    case _ =>

  }
}