package sieve

import akka.actor.{Actor, ActorRef, Props}
import sieve.Worker.NewPrime

object Worker {
  case class NewPrime(i: Int)
}

class Worker(localPrime: Int, N: Int) extends Actor {
  var nextPrime: ActorRef = null

  import Master._

  override def receive: Receive = beforeNewPrime

  def beforeNewPrime: Receive = {
    case i: Int if !divisibleByLocalPrime(i) => {
      context.actorSelection("/user/master") ! NewPrime(i) // notify master about the new prime
//      println(s"new prime $i") // print out new prime
      if (i > Math.sqrt(N)) {
        context.become(restArePrimes)
      }  else {
        nextPrime = context.actorOf(Props(new Worker(i, N))) // create next prime actor
        context.become(afterNewPrime)
      }
    }
    case End => context.actorSelection("/user/master") ! End // received End before forwarding any number => the final prime
  }

  def afterNewPrime: Receive = {
    case i: Int if !divisibleByLocalPrime(i) => {
      nextPrime ! i // forward to next prime
    }
    case End => nextPrime ! End // receive End from previous actor, notify next actor that no more number will be sent
  }

  // all received numbers are if not divisibleByLocalPrime will be prime, just notify master with NewPrime until End
  def restArePrimes: Receive = {
    case i: Int if !divisibleByLocalPrime(i) => context.actorSelection("/user/master") ! NewPrime(i) // notify master about the new prime
    case End => context.actorSelection("/user/master") ! End // the final prime
  }

  def divisibleByLocalPrime(x: Int) = (x % localPrime) == 0

}