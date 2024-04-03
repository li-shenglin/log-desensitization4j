package com.github.li_shenglin.desensitization.mask;

/**
 * 掩码转换
 *
 * @author shenglin.li  2024/4/2 20:13
 * @version 1.0
 */
public interface Mask {
    default char[] convert(char[] plainText){
        return convert(plainText, 0, plainText.length);
    }
    char[] convert(char[] plainText, int start, int end);
}
