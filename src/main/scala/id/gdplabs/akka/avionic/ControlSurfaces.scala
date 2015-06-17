package id.gdplabs.akka.avionic

import akka.actor.{ActorRef, Actor}

/**
 * Created by Deny Prasetyo
 * 18 June 2015
 * Principal Software Development Engineer
 * GDP Labs
 * deny.prasetyo@gdplabs.id
 */

object ControlSurfaces {

  case class StickBack(amount: Float)

  case class StickForward(amount: Float)

}

class ControlSurfaces(altimeter: ActorRef) extends Actor {

  import ControlSurfaces._
  import Altimeter._

  def receive = {
    case StickBack(amount) =>
      altimeter ! RateChange(amount)
    case StickForward(amount) =>
      altimeter ! RateChange(-1 * amount)
  }
}