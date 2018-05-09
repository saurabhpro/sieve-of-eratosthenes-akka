package sieve

import akka.actor.Actor
import Master._

class Sequential extends Actor {

  override def receive: Receive = {
    case Start(n) => {
      val tick = System.currentTimeMillis()
      val count = (1 to n).count(isPrime)
      val tock = System.currentTimeMillis()
      println(s"Sequential found number of primes up to $n is $count")
      println("Sequential method took: " + (tock - tick) + "ms")
    }
  }

  def isPrime(x: Int): Boolean = {
    if (x < 2) false
    else if (x == 2) true
    else !(2 to x-1).exists(i => x % i == 0)
  }
}


