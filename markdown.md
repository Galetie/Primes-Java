1. [Primes refresher](#primes-refresher)
2. [Testing for primes](#testing-for-primes)
3. [And now the efficiency considerations](#and-now-the-efficiency-considerations)
4. [One final optimization](#one-final-optimization)

View the source on [**GitHub**](https://github.com/Galetie/Primes-Java/tree/Part1)

## Primes refresher <a name="primes-refresher"></a>
Q. Why is the number 1 not a prime?  
A. Because the definition of a prime is:
> A positive integer **n** who has exactly 2 factors.

## Testing for primes <a name="testing-for-primes"></a>
Let's start off easy, how do we determine if a given number **n** is prime?
Well, according to the definition above we must check that it is:  
1. Is positive
2. Has exactly 2 factors

Alright well we can knock out the first requirement pretty easily,
```java
public class Primes {
    public static boolean isPrime(long candidate) {
        if (candidate <= 0) {
            return false; // Definitely not prime
        }
        
        // TODO: Check number of factors
        return true; // Maybe prime
    }
}
```
So, now we just need to consider how to check how many factors a number has.
Recall that a factor **y** of a number **n** is a factor if **n** divides
equally into **n**. Meaning **n mod y = 0**. Okay, so we just have to check each number
below **n** and check if it divides evenly; But before we do that, so we really want to
count how many factors it has? Well, yes, but also, no. We know that numbers with 2
factors (that is, prime numbers) are only ever 1 and **n** itself, so what we really
should check for is "Does this number **n** divide evenly into any number between 1 and **n**
exclusive?"

```java
public class Primes {
    public static boolean isPrime(long candidate) {
        if (candidate <= 0) {
            return false; // Definitely not prime
        }
        
        // Check for factors
        for (long denominator = 2; denominator < candidate; denominator++) {
            if (candidate % denominator == 0) {
                return false; // 100% not a prime
            }
        }
        
        return true; // Absolutely and confidently prime
    }
}
```

Awesome, we're done! Not quite... There's a slight issue, according to this function 1 is prime.
Okay well we could just modify the integer check, that'd fix that issue, surely.
```java
public class Primes {
    public static boolean isPrime(long candidate) {
        if (candidate <= 1) {
            return false; // Definitely not prime
        }
        
        // Check for factors
        for (long denominator = 2; denominator < candidate; denominator++) {
            if (candidate % denominator == 0) {
                return false; // 100% not a prime
            }
        }
        
        return true; // Absolutely and confidently prime
    }
}
```
And we're done. We can now check if a number is prime, hooray!

## And now the efficiency considerations <a name="and-now-the-efficiency-considerations"></a>
Ask yourself, is this efficient? *Hint: no*  
So where is the inefficiency? When looking for inefficiencies, always start by
checking the loops. If we can improve the speed of a loop, we can significantly
improve the efficiency of our function. Consider the number **10**. Our loop
starts at 2, and will check up to 9. Let's write down those numbers and identify
if they are a factor of 10 or not.

| **potential factor** | 2   | 3   | 4   | 5   | 6   | 7   | 8   | 9   |
|----------------------|-----|-----|-----|-----|-----|-----|-----|-----|
| **is factor of 10?** | yes | no  | no  | yes | no  | no  | no  | no  |

Now, note that every number greater than half **n** is not a factor. And this is
true for all numbers. This makes sense of course, a number's greatest factor cannot 
be greater than half of its value. So let's change our function to only check up to
half the value of **n**.
```java
public class Primes {
    public static boolean isPrime(long candidate) {
        if (candidate <= 1) {
            return false; // Definitely not prime
        }
        
        // Check for factors
        for (long denominator = 2; denominator < (candidate / 2) + 1; denominator++) {
            if (candidate % denominator == 0) {
                return false; // 100% not a prime
            }
        }
        
        return true; // Absolutely and confidently prime
    }
}
```
- Note that the limit is set to half n plus 1.  
This ensures we consider at least up to half plus 1 for the number 4.

What else might we consider? Well even-ness. Consider that if a number is divisible by
an even number, it is also divisible by 2. This works the other way as well,
if a number is not divisible by 2, it is not divisible by any even number.
So we can simply check if a number is divisible by 2, then check every odd
number instead of every number. This will also allow us to remove the +1 to the limit
as we will no longer check even numbers!

```java
public class Primes {
    public static boolean isPrime(long candidate) {
        // Check if positive integer
        if (candidate <= 1) {
            return false; // Definitely not prime
        }

        // Check if even and not 2, Cancel out checking of even numbers
        if (candidate % 2 == 0 && candidate != 2) {
            return false;
        }

        // Check for factors
        for (long denominator = 3; denominator < candidate / 2; denominator += 2) {
            if (candidate % denominator == 0) {
                return false; // 100% not a prime
            }
        }

        return true; // Absolutely and confidently prime
    }
}
```

## One final optimization <a name="one-final-optimization"></a>
There's another small optimization we may make. Currently, our **BigO** is
**O(n/2)**. To identify the next optimization we must look for a pattern 
within the factors of various non-prime numbers.

| **Number** | **Factors**                |
|:----------:|----------------------------|
|   **10**   | 1, 2, 5, 10                |
|   **18**   | 1, 2, 3, 6, 9, 18          |
|   **24**   | 1, 2, 3, 4, 6, 8, 12, 24   |
|   **28**   | 1, 2, 4, 7, 14, 28         |
|   **30**   | 1, 2, 3, 5, 6, 10, 15, 30. |

Something we may infer is that the factors of each number appear as pairs.
For example, 1 * 10 = 10, 2 * 5 = 10. So we only have to check up to half of a numbers
real factors, so for example for the number 30, we really only need to check up to and including
the number 5. For 28, up to and including 4. For 24, up to and including 4.  
As it turns out, the square root of n ends up being the upper limit for the highest number
we should ever need to check, so let's make the change and use the square root of n
instead.

```java
public class Primes {
    public static boolean isPrime(long candidate) {
        // Check if positive integer
        if (candidate <= 1) {
            return false; // Definitely not prime
        }

        // Check if even and not 2, Cancel out checking of even numbers
        if (candidate % 2 == 0 && candidate != 2) {
            return false;
        }

        // Check for factors
        for (long denominator = 3; denominator <= Math.sqrt(candidate); denominator += 2) {
            if (candidate % denominator == 0) {
                return false; // 100% not a prime
            }
        }

        return true; // Absolutely and confidently prime
    }
}
```

Alright well that's how we're going to check if a given number is prime.
