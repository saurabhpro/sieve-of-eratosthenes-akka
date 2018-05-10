package sieve

import akka.actor.{Actor, Props}

object Master {
  case class Start(N : Int)
  case object Stop
  case object End
}

class Master extends Actor {
  var count = 1
  var N = 0
  var tick, tock: Long = _
  import Master._
  import Worker._
//  println(self.path)

  override def receive: Receive = {
    case Start(n) => {
      N = n
      // sequential method
      val seq = context.actorOf(Props(new Sequential))
      seq ! Start(n)

      // sieve of Eratothenes method
      tick = System.currentTimeMillis()
      val prime2 = context.actorOf(Props(new Worker(2, n)))
      (3 to n).foreach(prime2 ! _)
      prime2 ! End // after sending all numbers, notify prime2 that no more number will be sent
    }
    case Stop => {
      println("Stopping...")
      context.stop(self)
    }
    case End => { // worker actor notify no more primes
      tock = System.currentTimeMillis()
      println(s"Sieve of Eratothenes method found the number of primes up to $N is $count after ${tock - tick}ms")
      val estimate = primeNumberTheorem(N) // estimation from prime number theorem
      val accuracy = 100 - 100.0 * Math.abs(estimate - count) / count
      println(f"Prime number theorem estimated there are $estimate prime numbers up to $N, a $accuracy%.2f%% accuracy")
    }
    case NewPrime(_) => count += 1 // Worker found a new prime, increase count
  }

  def primeNumberTheorem(n : Int) = Math.round(n / Math.log(n))
}
