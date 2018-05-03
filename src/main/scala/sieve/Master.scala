package sieve

import akka.actor.{Actor, Props}

object Master {
  case object Start
  case object Stop
}

class Master extends Actor {
  import Master._

  override def receive: Receive = {
    case Start => {
      val N = 100
      val prime2 = context.actorOf(Props(new Worker(2)))
      (3 to N).foreach(prime2 ! _)

    }

    case Stop =>
      context.stop(self)
  }
}
