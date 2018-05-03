package sieve

import akka.actor.{ActorSystem, Props}
import sieve.Master._

object Main extends App {
  val system = ActorSystem("sieve")
  val myActor = system.actorOf(Props[Master], "master")

  myActor ! Start
  Thread.sleep(10000)
  myActor ! Stop
}
