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