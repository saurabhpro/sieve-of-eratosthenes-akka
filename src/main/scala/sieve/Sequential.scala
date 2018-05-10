package sieve

import akka.actor.{Actor, ActorRef}
import Master._

class Sequential(master: ActorRef) extends Actor {

  override def receive: Receive = {
    case Start(n) => {
      val tick = System.currentTimeMillis()
      val count = (1 to n).count(isPrime)
      val tock = System.currentTimeMillis()
//      println(s"Sequential method found the number of primes up to $n is $count after ${tock - tick}ms")
      master ! Result("sequential", n, count, tock - tick) // report result to master
    }
  }

  def isPrime(x: Int): Boolean = {
    if (x < 2) false
    else if (x == 2) true
    else !(2 to x-1).exists(i => x % i == 0)
  }
}


