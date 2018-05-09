package sieve

import akka.actor.{Actor, ActorRef, Props}

object Worker {
  case object End
}

class Worker(localPrime: Int, N: Int) extends Actor {
  var nextPrime: ActorRef = null
  import Master._

  override def receive: Receive = beforeNewPrime

  def beforeNewPrime: Receive = {
//    case i: Int if i > Math.sqrt(N) => {
//      context.actorSelection("/user/master") ! i
//      context.become(restArePrimes)
//    }

    case i: Int => if (i< N & i%localPrime != 0) {
      nextPrime = context.actorOf(Props(new Worker(i, N)))
      context.actorSelection("/user/master") ! i
//      println(i)
      nextPrime ! i
      context.become(afterNewPrime)
    }
    case End => context.actorSelection("/user/master") ! End
  }

  def afterNewPrime: Receive = {
    case i: Int => if (i<N & i%localPrime != 0) {
      nextPrime ! i
    }
    case End => nextPrime ! End
  }

  def restArePrimes: Receive = {
    case i: Int => context.actorSelection("/user/master") ! i
  }





}
