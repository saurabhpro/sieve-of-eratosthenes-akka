package sieve

import akka.actor.{Actor, Props}

object Master {
  case class Start(N : Int)
  case object Stop
  case object End
}

class Master extends Actor {
  var count = 1
//  val N = 500000
  var N = 0
  var tick, tock: Long = _
  import Master._
  println(self.path)
  override def receive: Receive = {
    case Start(n) => {
      N = n
      val seq = context.actorOf(Props(new Sequential))
      seq ! Start(n)

      tick = System.currentTimeMillis()
      val prime2 = context.actorOf(Props(new Worker(2, N)))
      (3 to N).foreach(prime2 ! _)
      prime2 ! End

    }
    case Stop => {
      println("Stopping master")
      context.stop(self)
    }
    case End => {
      tock = System.currentTimeMillis()
      println(s"Number of primes up to $N is $count")
      println("Elapsed time: " + (tock - tick) + "ms")
    }
    case _ => count += 1
  }
}
