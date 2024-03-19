package com.ca.mfd.prc.common.utils;

import java.util.UUID;

public class SerialNoHelper {

    private SerialNoHelper() {
    }

    public static String initializeSerialNo() {
        long i = 1;
        byte[] byteArray = UUID.randomUUID().toString().getBytes();
        for (byte b : byteArray) {
            i *= (b + 1);
        }
        return String.format("%x", i - System.currentTimeMillis());
    }
}
