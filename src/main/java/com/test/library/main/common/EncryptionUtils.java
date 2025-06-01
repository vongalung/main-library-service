package com.test.library.main.common;

import static java.util.Base64.getEncoder;

import java.util.Arrays;
import java.util.Base64;

public class EncryptionUtils {
    static final Base64.Encoder encoder = getEncoder();

    public static byte[] encrypt(String text) {
        return encoder.encode(text.getBytes());
    }

    public static boolean verifyEncryptedMatches(byte[] encrypted, String text) {
        return Arrays.equals(encrypted, encrypt(text));
    }
}
