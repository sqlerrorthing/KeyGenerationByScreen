package me.oneqxz.keygeneration;

import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class KeyGeneration {

    private static int lastMouseX, lastMouseY;
    private static final int steps = 100;

    private static final StepInfo[] stepsInfo = new StepInfo[steps];

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("Please move your mouse across the screen");

        for (int step = 0; step < steps;)
        {
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            Point mouseLocation = pointerInfo.getLocation();

            final int mouseX = (int) mouseLocation.getX();
            final int mouseY = (int) mouseLocation.getY();

            if(mouseX != lastMouseX || mouseY != lastMouseY)
            {
                lastMouseX = mouseX;
                lastMouseY = mouseY;

                stepsInfo[step] = new StepInfo() {
                    @Override
                    public int getMouseX() {
                        return mouseX;
                    }

                    @Override
                    public int getMouseY() {
                        return mouseY;
                    }
                };

                step++;
                System.out.print("\rStep " + step + " of " + steps + " completed!");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        StringBuilder keyString = new StringBuilder();

        Arrays.stream(stepsInfo).map(StepInfo::toXYString).forEach(keyString::append);
        System.out.println("\n\nOUT: " + keyString);
        System.out.println("SHA256: " + genSHAKey(keyString.toString()));
        System.out.println();
        System.out.println("128-Bytes AES Key: " + getAES128Key(keyString.toString(), 16));
        System.out.println("256-Bytes AES Key: " + getAES128Key(keyString.toString(), 32));
    }

    private static String getAES128Key(String keyString, int k) throws NoSuchAlgorithmException {
        byte[] bytes = keyString.getBytes();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(bytes);

        byte[] key = Arrays.copyOf(hash, k);

        StringBuilder result = new StringBuilder();
        for (byte b : key) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    private static String genSHAKey(String sym)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sym.getBytes());

            // Преобразование хэша в строку в шестнадцатеричном формате
            StringBuilder hexHash = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException ignored) {}

        throw new IllegalStateException("Invalid hash algorithm");
    }
}
