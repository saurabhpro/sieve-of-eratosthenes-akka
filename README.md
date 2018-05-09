# Sieve of Eratothenes with Akka

**Problem:** How many primes are there less than an integer N?

## [Prime number theorem](https://www.khanacademy.org/computing/computer-science/cryptography/comp-number-theory/v/prime-number-theorem-the-density-of-primes)
![prime density](http://img.youtube.com/vi/7jzCJJIc59E/0.jpg)  

The prime number theorem, a formula that tell us without counting approximately the number of prime up to N, is `primes(N) = N / ln(N)`.  
For example, if we want to know the number of primes less than `100 trillion` (10^14), using the formula we got `~3.1 trillion`, compared to the actual count of `~3.2 trillion`, it's over `99.99%` accuracy.  
But what if we want to know the exact number?

## Actual count
### Sequential solution: 
For every number x up to N, check if it x is prime then increase the count variable, while x is prime if it isn't divisible by any number from 2 until x.  
This algorithm is very expensive and inefficient, because we repeat a lot of computation on different numbers.

### [Sieve of Eratosthenes](https://www.khanacademy.org/computing/computer-science/cryptography/comp-number-theory/v/sieve-of-eratosthenes-prime-adventure-part-4)
Elegant and simple solution from a ancient Greek philosopher Eratosthenes to generate all prime number up to N.
We simply turn this problem into counting prime numbers problem and implement using the Actor model with Akka.  
First, we create an actor `Sieve2` that receives all integers up to N (3, 4, 5, 6...N), then filters all numbers that are not divisible by 2. The next number that is not divisible by 2 is 3, `Sieve2` will create the next actor in the pipeline `Sieve3`, then pass all following numbers to `Sieve3`(5, 7, 9, 11...)... Using Akka actors, we create a pipeline that grows dynamically, and the number of actors created is the number of primes we need to find.  
Finally, using Akka cluster, we'll build a distributed system without changing the logic implementation.

