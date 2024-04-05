package io.github.li_shenglin.desensitization.core;

import java.util.Arrays;

/**
 * 匹配结果上下文
 *
 * @author shenglin.li  2024/4/3 01:39
 * @version 1.0
 */
public class MatchContext {
    private String plainText;
    private char[] result;

    public MatchContext(String plainText) {
        this.plainText = plainText;
        this.result = plainText.toCharArray();
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public char[] getResult() {
        return result;
    }

    public void setResult(char[] result) {
        this.result = result;
    }

    public String refresh() {
        if (result != null && result.length > 0) {
            plainText = String.valueOf(result);
        }
        return plainText;
    }
}
