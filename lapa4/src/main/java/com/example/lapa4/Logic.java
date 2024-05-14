package com.example.lapa4;

import java.math.BigInteger;

public class Logic {

    public int[] EEA(int a, int b)
    {
        // Исходные значения для уравнения ax + by = НОД(a, b)
        int x0 = 1, y0 = 0;
        int x1 = 0, y1 = 1;

        while (b != 0)
        {
            int q = a / b;
            int temp = b;
            b = a % b;
            a = temp;

            int xx = x0 - q * x1;
            int yy = y0 - q * y1;
            x0 = x1; y0 = y1;
            x1 = xx; y1 = yy;
        }
        return new int[]{a, x0, x1};
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
