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
      println(s"Sieve of Eratothenes method found the number of primes up to $N is $count after ${tock - tick}ms")
      val estimate = primeNumberTheorem(N)
      val accuracy = 100 - 100.0 * Math.abs(estimate - count) / count
      println(f"Prime number theorem estimated there are $estimate prime numbers up to $N, a $accuracy%.2f%% accuracy")
    }
    case _ => count += 1
  }

  def primeNumberTheorem(n : Int) = Math.round(n / Math.log(n))
}
