package id.gdplabs.akka.avionic

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.util.Timeout
import scala.concurrent.Await
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by Deny Prasetyo
 * 18 June 2015
 * Principal Software Development Engineer
 * GDP Labs
 * deny.prasetyo@gdplabs.id
 */


object Avionics extends App{
    // needed for '?' below
    implicit val timeout = Timeout(5.seconds)
    val system = ActorSystem("PlaneSimulation")
    val plane = system.actorOf(Props[Plane], "Plane")

      // Grab the controls
      val control = Await.result(
        (plane ? Plane.GiveMeControl).mapTo[ActorRef],
        5.seconds)
      // Takeoff!
      system.scheduler.scheduleOnce(200.millis) {
        control ! ControlSurfaces.StickBack(1f)
      }
      // Level out
      system.scheduler.scheduleOnce(1.seconds) {
        control ! ControlSurfaces.StickBack(0f)
      }
      // Climb
      system.scheduler.scheduleOnce(3.seconds) {
        control ! ControlSurfaces.StickBack(0.5f)
      }
      // Level out
      system.scheduler.scheduleOnce(4.seconds) {
        control ! ControlSurfaces.StickBack(0f)
      }
      // Shut down
      system.scheduler.scheduleOnce(5.seconds) {
        system.shutdown()
      }
}
