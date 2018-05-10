package sieve

import akka.actor.{Actor, Props, ActorRef}


class SieveMaster(master: ActorRef) extends Actor {
  var count = 1
  var N = 0
  var tick, tock: Long = _
  import Master._
  import Worker._

  override def receive: Receive = {
    case Start(n) => {
      N = n
      tick = System.currentTimeMillis()
      val prime2 = context.actorOf(Props(new Worker(2, n, self)))
      (3 to n).foreach(prime2 ! _)
      prime2 ! End // after sending all numbers, notify prime2 that no more number will be sent
    }
    case End => { // worker actor notify no more primes
      tock = System.currentTimeMillis()
      master ! Result("sieve", N, count, tock - tick) // report result to master
    }
    case NewPrime(_) => count += 1 // Worker found a new prime, increase count
  }

}
