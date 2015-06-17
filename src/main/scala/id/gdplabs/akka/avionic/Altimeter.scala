package id.gdplabs.akka.avionic

import akka.actor.{Actor, ActorLogging}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
 * Created by Deny Prasetyo
 * 18 June 2015
 * Principal Software Development Engineer
 * GDP Labs
 * deny.prasetyo@gdplabs.id
 */

object Altimeter {

  case class RateChange(amount: Float)

  case class AltitudeUpdate(altitude: Double)

}

class Altimeter extends Actor with ActorLogging with EventSource {

  import Altimeter._

  val ceiling = 400
  val maxRateOfClimb = 500
  var lastTick = System.currentTimeMillis
  val ticker = context.system.scheduler.schedule(100.millis, 100.millis,
    self, Tick)

  case object Tick

  var rateOfClimb: Float = 0
  var altitude: Double = 0

  def altimeterReceive: Receive = {
    case RateChange(amount) =>
      // Keep the value of rateOfClimb within [-1, 1]
      rateOfClimb = amount.min(1.0f).max(-1.0f) * maxRateOfClimb
      log.info(s"Altimeter changed rate of climb to $rateOfClimb.")
    case Tick =>
      val tick = System.currentTimeMillis
      altitude = altitude + ((tick - lastTick) / 60000.0) * rateOfClimb
      lastTick = tick
      sendEvent(AltitudeUpdate(altitude))
  }

  def receive = eventSourceReceive orElse altimeterReceive

  override def postStop(): Unit = ticker.cancel()
}
