package sieve

import akka.actor.{ActorSystem, Props}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.RoundRobinPool
import com.typesafe.config.ConfigFactory
import sieve.Master._

object Main extends App {
  val config = ConfigFactory.load()
  val system = ActorSystem("sieveoferatosthenes")
  val myActor = system.actorOf(Props[Master], "master")
  myActor ! Start(100000)

  // run jar with command java -DPORT=2551 -jar SieveOfEratosthenes-assembly-0.1.jar
  // this will create seed node and router towards the cluster (see below)
  // (to create jar, go to view -> tool windows -> sbt shell, and issue command 'assembly')
  // start other cluster node by using port number other than 2551 and 2555
  // to start traffic generation, run the jar with port number 2555 (see code below)
  if (config.getString("akka.remote.netty.tcp.port") == "2551") {
    println(s"now creating a router towards node actors")
    val router = system.actorOf(ClusterRouterPool(
      local = RoundRobinPool(8),
      settings = ClusterRouterPoolSettings(
        totalInstances = 15,
        maxInstancesPerNode = 4,
        allowLocalRoutees = false
      )
    ).props(Props[Master]),
      name = "routeractor")

    println(s"router: ${router.path}")
  }

  if (config.getString("akka.remote.netty.tcp.port") == "2555") {
    Thread.sleep(5000)

    val router = system.actorSelection("akka.tcp://sieveoferatosthenes@127.0.0.1:2551/user/routeractor")

    (10000 to 200000 by 10000).foreach(f = (i) => {
      router ! Start(i)
      Thread.sleep(500)
    })

  }

}