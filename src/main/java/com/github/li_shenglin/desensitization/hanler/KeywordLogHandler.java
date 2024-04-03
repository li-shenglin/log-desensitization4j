package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;
import com.github.li_shenglin.desensitization.mask.Mask;
import com.github.li_shenglin.desensitization.mask.PositionMask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 关键字类的抽象
 * @author shenglin.li  2024/4/3 17:03
 * @version 1.0
 */
public class KeywordLogHandler implements LogHandler {
        protected final Set<String> keywords;
        protected final Mask maskHandler;
        protected final Set<Character> endSymbols = new HashSet<>(Arrays.asList(' ', ',', '|', '，'));

    public KeywordLogHandler(String[] keywords, Mask maskHandler) {
        this.keywords = new HashSet<>(Arrays.asList(keywords));
        this.maskHandler = maskHandler;
    }

    @Override
    public void handler(MatchContext context) {
        keywords.forEach(keyword -> {
            int idx = 0;
            while ((idx = context.getPlainText().indexOf(keyword, idx)) != -1) {
                idx = mask(context.getResult(), idx + keyword.length() + 1);
            }
        });
    }

    private int mask(char[] msg, int start) {
        while (start < msg.length && msg[start] == ' ') {
            start++;
        }
        if (start >= msg.length) {
            return start;
        }

        int left = start;
        int p = left;
        while (p < msg.length) {
            if (endSymbols.contains(msg[p])) {
                break;
            }
            p++;
        }
        maskHandler.convert(msg, left, p);
        return p + 1;
    }
}
