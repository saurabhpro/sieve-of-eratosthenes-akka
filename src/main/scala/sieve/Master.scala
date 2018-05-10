package sieve

import akka.actor.{Actor, Props}

object Master {
  case class Start(N : Int)
  case class Result(method: String, N: Int, count: Int, time: Long)
  case object Stop
  case object End

}

class Master extends Actor {
  import Master._

  override def receive: Receive = {
    case Start(n) => {
      // sequential method
      val seq = context.actorOf(Props(new Sequential(self)))
      seq ! Start(n)

      // sieve of Eratothenes method
      val sieve = context.actorOf(Props(new SieveMaster(self)))
      sieve ! Start(n)
    }
    case Stop => {
      println("Stopping...")
      context.stop(self)
    }
    case Result(method, n, count, time) if method == "sieve" => { // receive Result from SieveMaster
      println(s"Sieve of Eratothenes method found the number of primes up to $n is $count after ${time}ms")
      val estimate = primeNumberTheorem(n) // estimation from prime number theorem
      val accuracy = 100 - 100.0 * Math.abs(estimate - count) / count
      println(f"Prime number theorem estimated there are $estimate prime numbers up to $n, a $accuracy%.2f%% accuracy")
    }
    case Result(method, n, count, time)  => { // receive Result from Sequential method
      println(s"Sequential method found the number of primes up to $n is $count after $time ms")
    }
  }

  // estimate the number of primes up to n
  def primeNumberTheorem(n : Int) = Math.round(n / Math.log(n))
}
