package sieve

import akka.actor.{ActorSystem, Props}
import sieve.Master._

object Main extends App {
  val system = ActorSystem("sieve")
  val myActor = system.actorOf(Props[Master], "master")

  myActor ! Start(100000)

}