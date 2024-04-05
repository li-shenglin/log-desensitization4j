package io.github.li_shenglin.desensitization.mask;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author shenglin.li  2024/4/2 23:05
 * @version 1.0
 */
class PositionMaskTest {

    @Test
    void convertJustLeft() {
        String plainText = "0123456789";
        PositionMask positionMaskConvertor = new PositionMask(3);
        char[] s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("012*******", new String(s));

        positionMaskConvertor = new PositionMask(-3);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("***3456789", new String(s));

        positionMaskConvertor = new PositionMask(0);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("**********", new String(s));
    }

    @Test
    void convertJustRight() {
        String plainText = "0123456789";
        PositionMask positionMaskConvertor = new PositionMask(0,3);
        char[] s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("*******789", new String(s));

        positionMaskConvertor = new PositionMask(0,-3);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("0123456***", new String(s));

        positionMaskConvertor = new PositionMask(0,0);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("**********", new String(s));
    }

    @Test
    void convertLeftAndRight() {
        String plainText = "0123456789";
        PositionMask positionMaskConvertor = new PositionMask(3,3);
        char[] s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("012****789", new String(s));

        positionMaskConvertor = new PositionMask(-2,-3);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("**23456***", new String(s));

        positionMaskConvertor = new PositionMask(3,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("012*******", new String(s));

        positionMaskConvertor = new PositionMask(-3,2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("********89", new String(s));
    }

    @Test
    void convertLeftAndRightWithException() {
        String plainText = "123";
        PositionMask positionMaskConvertor = new PositionMask(3,3);
        char[] s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("123", new String(s));

        positionMaskConvertor = new PositionMask(-2,-3);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("***", new String(s));

        positionMaskConvertor = new PositionMask(3,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("123", new String(s));

        positionMaskConvertor = new PositionMask(-3,2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("*23", new String(s));
    }

    @Test
    void convertPotion() {
        String plainText = "0123456789@abcde.com";
        String positionChar = "@";

        PositionMask positionMaskConvertor = new PositionMask(3, positionChar);
        char[] s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("*******789@abcde.com", new String(s));

        positionMaskConvertor = new PositionMask(-2, positionChar);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("01234567**@abcde.com", new String(s));

        positionMaskConvertor = new PositionMask(positionChar,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("0123456789@**cde.com", new String(s));

        positionMaskConvertor = new PositionMask(positionChar,2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("0123456789@ab*******", new String(s));

        positionMaskConvertor = new PositionMask(3, positionChar, 2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("*******789@ab*******", new String(s));

        positionMaskConvertor = new PositionMask(-2, positionChar, 2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("01234567**@ab*******", new String(s));

        positionMaskConvertor = new PositionMask(3, positionChar,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("*******789@**cde.com", new String(s));

        positionMaskConvertor = new PositionMask(-3, positionChar,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray());
        assertEquals("0123456***@**cde.com", new String(s));
    }

    @Test
    void convertPotion1() {
        String plainText = "email=0123456789@abcde.com endX";
        String positionChar = "@";

        PositionMask positionMaskConvertor = new PositionMask(3, positionChar);
        char[] s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=*******789@abcde.com endX", new String(s));

        positionMaskConvertor = new PositionMask(-2, positionChar);
        s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=01234567**@abcde.com endX", new String(s));

        positionMaskConvertor = new PositionMask(positionChar,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=0123456789@**cde.com endX", new String(s));

        positionMaskConvertor = new PositionMask(positionChar,2);
        s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=0123456789@ab******* endX", new String(s));

        positionMaskConvertor = new PositionMask(3, positionChar, 2);
        s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=*******789@ab******* endX", new String(s));

        positionMaskConvertor = new PositionMask(-2, positionChar, 2);
        s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=01234567**@ab******* endX", new String(s));

        positionMaskConvertor = new PositionMask(3, positionChar,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=*******789@**cde.com endX", new String(s));

        positionMaskConvertor = new PositionMask(-3, positionChar,-2);
        s = positionMaskConvertor.convert(plainText.toCharArray(), 6, 26);
        assertEquals("email=0123456***@**cde.com endX", new String(s));
    }
}
