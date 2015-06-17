package id.gdplabs.akka.avionic

import akka.actor.{Props, ActorLogging, Actor}
import id.gdplabs.akka.avionic.Altimeter.AltitudeUpdate
import id.gdplabs.akka.avionic.EventSource.RegisterListener
import id.gdplabs.akka.avionic.Plane.GiveMeControl

/**
 * Created by Deny Prasetyo
 * 18 June 2015
 * Principal Software Development Engineer
 * GDP Labs
 * deny.prasetyo@gdplabs.id
 */


object Plane {

  case object GiveMeControl

}

class Plane extends Actor with ActorLogging {


  val altimeter = context.actorOf(Props[Altimeter])
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)))

  override def preStart() {
    altimeter ! RegisterListener(self)
  }

  def receive = {
    case AltitudeUpdate(altitude) =>
      log.info(s"Altitude is now: $altitude")
    case GiveMeControl =>
      log.info("Plane giving control.")
      sender ! controls
  }
}