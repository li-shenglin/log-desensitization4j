package com.github.li_shenglin.desensitization.mask;

import java.util.Objects;

/**
 * 自定义掩码规则
 *
 * @author shenglin.li  2024/4/5 8:29
 * @version 1.0
 */
public class CustomMask implements Mask {
    private Mask mask;

    public CustomMask(Mask mask) {
        this.mask = Objects.requireNonNull(mask);
    }

    @Override
    public char[] convert(char[] plainText, int start, int end) {
        return mask.convert(plainText, start, end);
    }

    @Override
    public char[] convert(char[] plainText) {
        return mask.convert(plainText);
    }
}
