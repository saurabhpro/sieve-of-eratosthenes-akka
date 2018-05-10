# Sieve of Eratosthenes with Akka

**Problem:** How many primes are there less than an integer N?

## [Prime number theorem](https://www.khanacademy.org/computing/computer-science/cryptography/comp-number-theory/v/prime-number-theorem-the-density-of-primes)
![prime density](http://img.youtube.com/vi/7jzCJJIc59E/0.jpg)  

The prime number theorem, a formula that tell us without counting approximately the number of prime up to N, is `primes(N) = N / ln(N)`.  
For example, if we want to know the number of primes less than `100 trillion` (10^14), using the formula we got `~3.1 trillion`, compared to the actual count of `~3.2 trillion`, it's over `99.99%` accuracy.  
But what if we want to know the exact number?

## Actual count
### Sequential solution: 
For every number `x` up to `N`, check if it `x` is prime then increase the `count` variable, while `x` is prime if it isn't divisible by any number from 2 until `x`.  
This algorithm is very expensive and inefficient, because we repeat a lot of computation on different numbers.

### [Sieve of Eratosthenes](https://www.khanacademy.org/computing/computer-science/cryptography/comp-number-theory/v/sieve-of-eratosthenes-prime-adventure-part-4)
![Eratosthenes](https://upload.wikimedia.org/wikipedia/commons/b/b3/Eratosthene.01.png)  
An elegant and simple solution from a ancient Greek philosopher Eratosthenes to generate all prime numbers up to N.
We simply turn this problem into counting prime numbers problem and implement a parallel version using the Actor model with Akka.  
First, we create an actor `prime2` that receives all integers up to N (3, 4, 5, 6...N), then filters all numbers that are not divisible by 2. The next number that is not divisible by 2 is 3, `prime2` will create the next actor in the pipeline `prime3`, then pass all following numbers to `prime3`(5, 7, 9, 11...)... Using Akka actors, we create a pipeline that grows dynamically, and the number of actors created is the number of primes we need to find.  
```
Sieve of Eratothenes method found the number of primes up to 500000 is 41538 after 1586ms
Prime number theorem estimated there are 38103 prime numbers up to 500000, a 91,73% accuracy
Sequential method found the number of primes up to 500000 is 41538 after 107434ms
```  

Finally, using Akka cluster, we'll build a distributed system without changing the logic implementation.  
![cluster image](https://raw.githubusercontent.com/luonglearnstocode/sieve-of-eratosthenes-akka/master/cluster.png)
