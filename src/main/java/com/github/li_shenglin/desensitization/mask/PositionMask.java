package com.github.li_shenglin.desensitization.mask;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 根据位置生成掩码
 * 例子：
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor(4)           |   前四位明文其它显示掩码                     |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor(-8)          |   前8位* 其它显示明文                       |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor(3, 4)        |   前3位和后四位明文， 其它使用掩码            |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor(-3, -2)      |   前3位和后2位掩码， 其它使用明文             |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor(3, "@")      |   @ 左边3位明文， 其它使用掩码，右边明文       |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor(-3, "@")     |   @ 左边3位掩码， 其它使用明文，右边明文       |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor("@", 4)      |   @ 右边4位明文， 其它使用掩码，左边明文       |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor("@",-2)      |   @ 右边2位掩码， 其它使用明文，左边明文       |
 * ----------------------------------------------------------------------------<br/>
 * | PositionMaskConvertor(-3,"@",-2)   |   @ 右边2位掩码，左边3位掩码  其它使用明文     |
 * ----------------------------------------------------------------------------<br/>
 * 备注：字符 @ 都是指第一个
 *
 * @author shenglin.li  2024/4/2 23:33
 * @version 1.0
 */
public class PositionMask implements Mask {
    private final int positionLeft;
    private final int positionRight;

    private final String positionChar;
    private final char maskChar;

    public PositionMask() {
        this(0);
    }

    public PositionMask(int positionLeft) {
        this(positionLeft, 0);
    }

    public PositionMask(int positionLeft, int positionRight) {
        this(positionLeft, null, positionRight);
    }

    public PositionMask(int positionLeft, String positionChar) {
        this(positionLeft, positionChar, 0);
    }

    public PositionMask(String positionChar, int positionRight) {
        this(0, positionChar, positionRight);
    }

    public PositionMask(int positionLeft, String positionChar, int positionRight) {
        this(positionLeft, positionChar, positionRight, '*');
    }

    public PositionMask(int positionLeft, String positionChar, int positionRight, char maskChar) {
        Objects.requireNonNull(maskChar);
        this.positionLeft = positionLeft;
        this.positionRight = positionRight;
        this.positionChar = positionChar;
        this.maskChar = maskChar;
    }

    @Override
    public char[] convert(char[] plainText, int start, int end) {
        if (plainText == null || plainText.length == 0) {
            return plainText;
        }
        end = Math.min(end, plainText.length);

        if (positionChar == null || positionChar.trim().isEmpty()) {
            if (positionLeft > 0) {
                if (positionRight <= 0) {
                    return mask(plainText, start + positionLeft, end);
                } else {
                    return mask(plainText, start + positionLeft, end - positionRight);
                }
            } else if (positionLeft < 0) {
                if (positionRight > 0) {
                    return mask(plainText, start, end - positionRight);
                } else if (positionRight < 0) {
                    return mask(mask(plainText, start, -positionLeft),
                            end + positionRight, end);
                } else {
                    return mask(plainText, start, -positionLeft);
                }
            } else {
                if (positionRight > 0) {
                    return mask(plainText, start, end - positionRight);
                } else if (positionRight == 0) {
                    return mask(plainText, start, end);
                } else {
                    return mask(plainText, end + positionRight, end);
                }
            }
        }

        int pIndex = indexOf(plainText, positionChar, start, end);
        if (positionLeft > 0) {
            mask(plainText, start, pIndex - positionLeft);
        }
        if (positionLeft < 0) {
            mask(plainText, pIndex + positionLeft, pIndex);
        }

        if (positionRight > 0) {
            mask(plainText, pIndex + positionRight + 1, end);
        }
        if (positionRight < 0) {
            mask(plainText, pIndex + 1, pIndex + 1 - positionRight);
        }

        return plainText;
    }

    private int indexOf(char[] arr, String c, int start, int end) {
        int last = Math.min(end, arr.length);
        for (int i = start; i < last; i++) {
            int j = 0;
            for (j = 0; j < c.length() && i + j < arr.length; j++) {
                if (arr[i + j] != c.charAt(j)) {
                    break;
                }
            }

            if (j == c.length()) {
                return i;
            }
        }
        return -1;
    }

    private char[] mask(char[] plainText, int start, int end) {
        int last = Math.min(plainText.length, end);
        if (start >= last || start < 0) {
            return plainText;
        }
        while (start < last) {
            plainText[start] = maskChar;
            start++;
        }
        return plainText;
    }


    public static PositionMask build(String symbol) {
        if ("mask".equals(symbol)) {
            return new PositionMask();
        }
        if (symbol.startsWith("mask(") && symbol.endsWith(")")) {
            String string = symbol.substring(5, symbol.length() - 1);
            if (string.contains("<")) {
                int i = string.indexOf("<");
                String positionIdx = string.substring(i + 1);
                if (StringUtils.isNumeric(positionIdx)) {
                    return new PositionMask(Integer.parseInt(positionIdx), string.substring(0, i));
                }
            } else if (string.contains(">")) {
                int i = string.indexOf(">");
                String positionIdx = string.substring(i + 1);
                if (StringUtils.isNumeric(positionIdx)) {
                    return new PositionMask(string.substring(0, i), Integer.parseInt(positionIdx));
                }

            } else {
                String[] args = string.split(",");
                if (args.length == 1 && StringUtils.isNumeric(args[0])) {
                    return new PositionMask(Integer.parseInt(args[0]));
                } else if (args.length == 2 && StringUtils.isNumeric(args[0]) && StringUtils.isNumeric(args[1])) {
                    return new PositionMask(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                }
            }

        }
        throw new IllegalArgumentException("can`t parse desensitization[Mask]: " + symbol);
    }
}
