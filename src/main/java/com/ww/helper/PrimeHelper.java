package com.ww.helper;

import java.util.Arrays;

public class PrimeHelper {
    private static boolean[] primes = new boolean[10000];

    static {
        Arrays.fill(primes, true);
        primes[0] = primes[1] = false;
        for (int i = 2; i < primes.length; i++) {
            if (primes[i]) {
                for (int j = 2; i * j < primes.length; j++) {
                    primes[i * j] = false;
                }
            }
        }
    }

    public static int getPrimeFrom(int from) {
        for (int i = from; i < primes.length; i++) {
            if (primes[i]) {
                return i;
            }
        }
        return 3;
    }

    public static boolean isPrime(int n) {
        return primes[n];
    }
}
