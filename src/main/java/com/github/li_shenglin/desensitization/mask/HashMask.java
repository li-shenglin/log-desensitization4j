package com.github.li_shenglin.desensitization.mask;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * hash掩码
 *
 * @author shenglin.li  2024/4/2 21:37
 * @version 1.0
 */
public class HashMask implements Mask {
    private String salt;
    public HashMask() {
        this(null);
    }
    public HashMask(String salt) {
        if (salt != null ) {
            this.salt = salt;
        }
    }
    @Override
    public char[] convert(char[] plainText, int start, int end) {
        if (plainText == null || plainText.length == 0) {
            return plainText;
        }
        start = Math.max(start, 0);
        end = Math.min(end, plainText.length);
        if (start >= end) {
            return plainText;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            if (salt != null) {
                digest.update(salt.getBytes()); // 将盐值混合到哈希函数中
            }
            byte[] hashedBytes = digest.digest(new String(plainText, start, end - start).getBytes());

            BigInteger bigInteger = new BigInteger(1, hashedBytes);
            String dg = bigInteger.toString(16);

            for (int i = start; i < end; i++) {
                if(i-start < dg.length()) {
                    plainText[i] = dg.charAt(i-start);
                } else {
                    plainText[i] = '0';
                }
            }
            return plainText;
        } catch (Exception e) {
            return "".toCharArray();
        }
    }

    public static HashMask build(String symbol) {
        if ("hash".equals(symbol)) {
            return new HashMask();
        }
        if (symbol.startsWith("hash(") && symbol.endsWith(")")) {
            return new HashMask(symbol.substring(5, symbol.length() - 1));
        }
        throw new IllegalArgumentException("can`t parse desensitization[Hash]: " + symbol);
    }
}
