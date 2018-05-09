package sieve

import akka.actor.{Actor, Props}

object Master {
  case object Start
  case object Stop
}

class Master extends Actor {
  var count = 1
  val N = 100
  import Master._
  println(self.path)
  override def receive: Receive = {
    case Start => {
      val prime2 = context.actorOf(Props(new Worker(2, N)))
      (3 to N).foreach(prime2 ! _)

    }
    case Stop => {
      println(s"Number of primes up to $N is $count")
      context.stop(self)
    }
    case _ => count += 1
  }
}
