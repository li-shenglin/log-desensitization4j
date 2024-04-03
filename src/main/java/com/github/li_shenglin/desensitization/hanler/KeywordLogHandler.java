package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;
import com.github.li_shenglin.desensitization.mask.Mask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 关键字类的抽象
 *
 * @author shenglin.li  2024/4/3 17:03
 * @version 1.0
 */
public class KeywordLogHandler extends AbsLogHandler implements LogHandler {
    protected final Set<String> keywords;

    protected final Set<Character> endSymbols;

    public KeywordLogHandler(String[] keywords, Mask maskHandler) {
        this(keywords, null, maskHandler);
    }
    public KeywordLogHandler(String[] keywords, Character[] endSymbol, Mask maskHandler) {
        super(maskHandler);
        this.keywords = new HashSet<>(Arrays.asList(keywords));
        if (endSymbol == null || endSymbol.length == 0) {
            endSymbols = new HashSet<>(Arrays.asList(' ', ',', '|', '，'));
        } else {
            this.endSymbols = new HashSet<>(Arrays.asList(endSymbol));
        }
    }

    @Override
    public void handler(MatchContext context) {
        keywords.forEach(keyword -> {
            int idx = 0;
            while ((idx = context.getPlainText().indexOf(keyword, idx)) != -1) {
                idx = mask(context, idx + keyword.length() + 1);
            }
        });
    }

    private int mask(MatchContext context, int start) {
        char[] msg = context.getResult();
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
        context.setResult(maskHandler.convert(msg, left, p));
        return p + 1;
    }
}
