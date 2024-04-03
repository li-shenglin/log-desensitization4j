package com.github.li_shenglin.desensitization.mask;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * @author shenglin.li  2024/4/2 22:49
 * @version 1.0
 */
class HashMaskTest {

    @Test
    void convert() {
        char[] plainText = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890".toCharArray();
        HashMask convertor = new HashMask();
        char[] s = convertor.convert(plainText);
        assertArrayEquals(s,"b20e12a7bcf7a0bcc5150265aab9c40b1d673781c143a73be76232d81e6038ec000000000000000000000000000000000000".toCharArray());

        convertor = new HashMask("asdas");
        s = convertor.convert(plainText);
        assertArrayEquals(s,"465cc9644120ba9e587c5bfa12eadf7a1a2ff5d32404ac7070434b87c73ccc34000000000000000000000000000000000000".toCharArray());
    }

    @Test
    void convert1() {
        String plainText = "aa1234567890aa";
        HashMask convertor = new HashMask();
        char[] s = convertor.convert(plainText.toCharArray(), 2, 12);
        assertArrayEquals(s,"aac775e7b757aa".toCharArray());

        convertor = new HashMask("asdas");
        s = convertor.convert(plainText.toCharArray(), 2, 12);
        assertArrayEquals(s,"aab20a1a2509aa".toCharArray());
    }
}
