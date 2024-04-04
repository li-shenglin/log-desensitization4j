package com.github.li_shenglin.desensitization.mask;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author shenglin.li  2024/4/4 17:02
 * @version 1.0
 */
class FixedMaskTest {

    @Test
    void convert() {
        String plainText = "email=0123456789@abcde.com endX";
        FixedMask positionMaskConvertor = new FixedMask("XA");
        char[] s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=XA endX", new String(s));
    }

    @Test
    void convert1() {
        String plainText = "email=0123456789@abcde.com endX";
        FixedMask positionMaskConvertor = new FixedMask(2);
        char[] s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=XA endX".length(), s.length);
    }
}