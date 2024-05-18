package com.example.lapa4;

import java.math.BigInteger;

public class Logic {

        public static int extendedGCD(int a, int b, int[] xy) {
            if (a == 0) {
                xy[0] = 0;
                xy[1] = 1;
                return b;
            }

            int[] xy1 = new int[2];
            int gcd = extendedGCD(b % a, a, xy1);

            xy[0] = xy1[1] - (b / a) * xy1[0];
            xy[1] = xy1[0];

            return gcd;
        }

        public static int checkD(int p, int q, int d) {
            int r = p * q; // Вычисляем произведение p и q
            int phiR = (p - 1) * (q - 1); // Вычисляем значение функции Эйлера для r

            int e;
            int[] xy = new int[2];
            for (e = 2; e < phiR; e++) {
                if (extendedGCD(e, phiR, xy) == 1) {
                    boolean isCorrect = (e * d) % phiR == 1;
                    if (isCorrect) {
                        return e;
                    }
                }
            }
            return -30; // Проверяем условие e * d ≡ 1 (mod φ(r))
        }

    public boolean isPrimeNumber(int number) {
        BigInteger bigInteger = BigInteger.valueOf(number);
        return (bigInteger.isProbablePrime((int) Math.log(number)))&&(number!=1);
    }

    public boolean isRelativelyPrime(int num1, int num2){
        BigInteger bigInteger1 = BigInteger.valueOf(num1);
        BigInteger bigInteger2 = BigInteger.valueOf(num2);

        return bigInteger1.gcd(bigInteger2).intValue() == 1;
    }

    public int[] fromByteArray2IntArray(byte[] byteArray){
        int[] intArray = new int[byteArray.length];
        for (int k = 0; k < byteArray.length; k++) {
            byte b = byteArray[k];
            int i = 0;
            byte mask = 1;

            for (int j = 7; j >= 0; j--) {
                int temp = b & (mask << j);
                if (temp > 0) {
                    i ^= temp;
                }
            }
            intArray[k]=i;
        }
        return intArray;
    }
    public int powerMod(int base, int exponent, int modulus) {
        BigInteger bigIntegerBase = BigInteger.valueOf(base);
        BigInteger bigIntegerExponent = BigInteger.valueOf(exponent);
        BigInteger bigIntegerModulus = BigInteger.valueOf(modulus);

        return bigIntegerBase.modPow(bigIntegerExponent, bigIntegerModulus).intValue();
    }
    public int getHash(int[] input, int n){
        int hash=100;
        for (int i: input){
            hash = powerMod(hash+i, 2, n);
        }
        return hash;
    }
}
