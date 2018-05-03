package sieve

import akka.actor.{Actor, ActorRef, Props}

class Worker(localPrime: Int) extends Actor {
  var nextPrime: ActorRef = null

  override def receive: Receive = beforeNewPrime

  def beforeNewPrime: Receive = {
    case i: Int => if (i%localPrime != 0) {
      nextPrime = context.actorOf(Props(new Worker(i)))
      println(i)
      nextPrime ! i
      context.become(afterNewPrime)
    }
  }

  def afterNewPrime: Receive = {
    case i: Int => if (i%localPrime != 0) {
      nextPrime ! i
    }
  }





}
