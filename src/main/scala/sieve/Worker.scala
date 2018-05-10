package sieve

import akka.actor.{Actor, ActorRef, Props}
import sieve.Worker.NewPrime

object Worker {
  case class NewPrime(i: Int)
}

class Worker(localPrime: Int, N: Int, master: ActorRef) extends Actor {
  var nextPrime: ActorRef = null

  import Master._

  override def receive: Receive = beforeNewPrime

  // after finding the next number that are not divisibleByLocalPrime, create next prime actor
  def beforeNewPrime: Receive = {
    case i: Int if !divisibleByLocalPrime(i) => {
      master ! NewPrime(i)
//      println(s"new prime $i") // print out new prime
      if (i > Math.sqrt(N)) {
        context.become(restArePrimes)
      }  else {
        nextPrime = context.actorOf(Props(new Worker(i, N, master))) // create next prime actor
        context.become(afterNewPrime)
      }
    }
    case End => master ! End
  }

  // forward received numbers that are not divisibleByLocalPrime to next prime actor until End
  def afterNewPrime: Receive = {
    case i: Int if !divisibleByLocalPrime(i) =>  nextPrime ! i // forward to next prime
    case End => nextPrime ! End // receive End from previous actor, notify next actor that no more number will be sent
  }

  // all received numbers are if not divisibleByLocalPrime will be prime, just notify master with NewPrime until End
  def restArePrimes: Receive = {
    case i: Int if !divisibleByLocalPrime(i) => master ! NewPrime(i) // notify master about the new prime
    case End => master ! End // the final prime
  }

  def divisibleByLocalPrime(x: Int) = (x % localPrime) == 0

}