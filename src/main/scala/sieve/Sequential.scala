package sieve

import akka.actor.{Actor, ActorRef}
import Master._

class Sequential(master: ActorRef) extends Actor {

  override def receive: Receive = {
    case Start(n) => {
      val tick = System.currentTimeMillis()
      val count = (1 to n).count(isPrime) // count all the prime number from 1 to n
      val tock = System.currentTimeMillis()
      master ! Result("sequential", n, count, tock - tick) // report result to master
    }
  }

  // check if number x is prime
  def isPrime(x: Int): Boolean = {
    if (x < 2) false
    else if (x == 2) true
    else !(2 to x-1).exists(i => x % i == 0)
  }
}


