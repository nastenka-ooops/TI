package com.example.lapa2;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

//TODO x23+x5+1
public class Logic {
    public static final int maxPower = 23;
    public static final int registerBit = 5;
    ArrayList<Byte> plainText = new ArrayList<>();
    ArrayList<Byte> register = new ArrayList<>(maxPower);
    ArrayList<Byte> encryptText = new ArrayList<>();
    byte[] encryptTextInBits;
    ArrayList<Byte> decryptText = new ArrayList<>();
    ArrayList<Byte> generateKey = new ArrayList<>();
    File encryptFile = new File("encryptFile.txt");
    File decryptFile;

    public ArrayList<Byte> convertBytesToBits(byte[] input){
        for (int i = 0; i < input.length; i++) {
            byte temp = input[i];
            byte mask = 1;
            if ((byte)(temp & (mask << 7)) < 0) {
                plainText.add((byte) 1);
            } else {
                plainText.add((byte) 0);
            }

            for (int j = 6; j >=0; j--) {
                if ((temp & (mask << j)) > 0) {
                    plainText.add((byte) 1);
                } else {
                    plainText.add((byte) 0);
                }

            }
        }
        return plainText;
    }

    public byte[] convertBitsToBytes(ArrayList<Byte> input){
        encryptTextInBits = new byte[input.size()/8];
        for (int i = 0; i < input.size();) {
            byte tempByte = 0;
            for (int j = 7; j >=0; j--) {
                byte tempBit = input.get(i);
                tempBit<<=j;
                tempByte+=tempBit;
                i++;
            }
            encryptTextInBits[(i+1)/8-1]=tempByte;
        }
        return encryptTextInBits;
    }

    public void setRegister(String key){
        register.clear();
        for (int i = 0; i < key.length(); i++) {
            register.add(Byte.parseByte(String.valueOf(key.charAt(i))));
        }
    }

    private Byte generateKeyElement(){
        int newElement;
        newElement = (register.get(register.size()-maxPower)^register.get(register.size()-registerBit));
        Byte keyElement = register.get(0);
        register.remove(0);
        register.add((byte) newElement);
        return keyElement;
    }

    public ArrayList<Byte> encrypt() throws IOException {
        for (int i = 0; i < plainText.size(); i++) {
            byte tempKeyElement = generateKeyElement();
            generateKey.add(tempKeyElement);
            int temp = (plainText.get(i))^(tempKeyElement);
            encryptText.add((byte) temp);
        }
        Files.write(encryptFile.toPath(),convertBitsToBytes(encryptText));
        return encryptText;
    }

    public ArrayList<Byte> decrypt() throws IOException {
        decryptFile = new File("decryptFile."+Main.extension);
        setRegister(Main.key);
        for (int i = 0; i < encryptText.size(); i++) {
            int temp =  (encryptText.get(i) ^ generateKeyElement());
            decryptText.add((byte) temp);
        }
        Files.write(decryptFile.toPath(),convertBitsToBytes(decryptText));
        return decryptText;
    }
}
