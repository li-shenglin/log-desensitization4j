package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;
import com.github.li_shenglin.desensitization.mask.Mask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 关键字类的抽象
 *
 * @author shenglin.li  2024/4/3 17:03
 * @version 1.0
 */
public class KeywordLogHandler extends AbsLogHandler implements LogHandler {
    protected final Set<String> keywords;

    protected final Set<Character> endSymbols;

    protected final boolean ignoreCase;

    public KeywordLogHandler(String[] keywords, Mask maskHandler) {
        this(keywords, null, maskHandler, true);
    }

    public KeywordLogHandler(String[] keywords, Mask maskHandler, boolean ignoreCase) {
        this(keywords, null, maskHandler, ignoreCase);
    }

    public KeywordLogHandler(String[] keywords, Character[] endSymbol, Mask maskHandler) {
        this(keywords, endSymbol, maskHandler, true);
    }

    public KeywordLogHandler(String[] keywords, Character[] endSymbol, Mask maskHandler, boolean ignoreCase) {
        super(maskHandler);
        List<String> list = Arrays.asList(keywords);
        if (ignoreCase) {
            list = list.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
        this.keywords = new HashSet<>(list);
        this.ignoreCase = ignoreCase;
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
            while ((idx = indexOf(context.getResult(), keyword, idx)) != -1) {
                idx = mask(context, idx + keyword.length() + 1);
            }
        });
    }

    private int indexOf(char[] arr, String c, int start) {
        int last = arr.length;
        for (int i = start; i < last; i++) {
            int j = 0;
            for (j = 0; j < c.length() && i+j < arr.length; j++) {
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
