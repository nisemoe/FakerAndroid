package com.facker.toolchain.utils;

import java.nio.ByteBuffer;

public class HexEncoding {
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private HexEncoding() {
    }

    public static String encode(byte[] data, int offset, int length) {
        StringBuilder result = new StringBuilder(length * 2);

        for(int i = 0; i < length; ++i) {
            byte b = data[offset + i];
            result.append(HEX_DIGITS[b >>> 4 & 15]);
            result.append(HEX_DIGITS[b & 15]);
        }

        return result.toString();
    }

    public static String encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    public static String encodeRemaining(ByteBuffer data) {
        return encode(data.array(), data.arrayOffset() + data.position(), data.remaining());
    }
}
