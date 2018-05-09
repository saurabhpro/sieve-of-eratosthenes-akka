package sieve

import akka.actor.{Actor, ActorRef, Props}

class Worker(localPrime: Int, N: Int) extends Actor {
  var nextPrime: ActorRef = null

  override def receive: Receive = beforeNewPrime

  def beforeNewPrime: Receive = {
    case i: Int => if (i< N & i%localPrime != 0) {
      nextPrime = context.actorOf(Props(new Worker(i, N)))
      context.actorSelection("/user/master") ! i
      println(i)
      nextPrime ! i
      context.become(afterNewPrime)
    }
  }

  def afterNewPrime: Receive = {
    case i: Int => if (i<N & i%localPrime != 0) {
      nextPrime ! i
    }
  }





}
