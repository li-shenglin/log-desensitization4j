package com.github.li_shenglin.desensitization.mask;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;

/**
 * 固定长度随机字符 或 固定字符串替换
 *
 * @author shenglin.li  2024/4/4 16:23
 * @version 1.0
 */
public class FixedMask implements Mask {
    private String maskStr;
    private Integer length;

    public FixedMask(String maskStr) {
        this.maskStr = Objects.requireNonNull(maskStr);
    }

    public FixedMask(Integer length) {
        this.length = Objects.requireNonNull(length);
    }

    @Override
    public char[] convert(char[] plainText, int start, int end) {
        String str = Objects.nonNull(maskStr) ? maskStr : RandomStringUtils.randomAlphanumeric(length);
        char[] charArray = str.toCharArray();
        int diff = charArray.length - (end  - start);
        if (diff == 0) {
            for (int i = start; i < end; i++) {
                plainText[i] = charArray[i - start];
            }
            return plainText;
        }
        char[] result = new char[plainText.length + diff];
        System.arraycopy(plainText, 0, result, 0, start);
        System.arraycopy(charArray, 0, result, start, charArray.length);
        System.arraycopy(plainText, end, result, start + charArray.length, plainText.length - end);
        return result;
    }

    public static FixedMask build(String symbol) {
        if ("fixed".equals(symbol)) {
            return new FixedMask("***");
        }
        if (symbol.startsWith("fixed(") && symbol.endsWith(")")) {
            String string = symbol.substring(5, symbol.length() - 1);
            if (string.length() > 3 && string.charAt(0) == '"' && string.charAt(string.length()-1) == '"') {
                return new FixedMask(string.substring(1, string.length() - 1));
            } else if (NumberUtils.isDigits(string)) {
                return new FixedMask(Integer.parseInt(string));
            }
            return new FixedMask(string);
        }
        throw new IllegalArgumentException("can`t parse desensitization[Fixed]: " + symbol);
    }
}
