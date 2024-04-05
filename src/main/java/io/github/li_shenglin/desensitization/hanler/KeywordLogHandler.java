package io.github.li_shenglin.desensitization.hanler;

import io.github.li_shenglin.desensitization.core.MatchContext;
import io.github.li_shenglin.desensitization.mask.Mask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 关键字类的抽象
 *
 * @author shenglin.li  2024/4/3 17:03
 * @version 1.0
 */
public class KeywordLogHandler extends AbsLogHandler implements LogHandler {
    private static final Set<String> DEFAULT_SYMBOLS = new HashSet<>(Arrays.asList(" ", ":", "=", "->", "[", "("));

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
        this.ignoreCase = ignoreCase;
        if (endSymbol == null || endSymbol.length == 0) {
            endSymbols = new HashSet<>(Arrays.asList(' ', ',', '|', '，', ']', ')'));
        } else {
            this.endSymbols = new HashSet<>(Arrays.asList(endSymbol));
        }

        this.keywords = Arrays.stream(keywords)
                .map(keyword -> ignoreCase ? keyword.toLowerCase() : keyword)
                .collect(Collectors.toSet());
    }

    @Override
    public void handler(MatchContext context) {
        keywords.forEach(keyword -> {
            int idx = 0;
            while ((idx = indexEndOf(context.getResult(), keyword, idx, context.getResult().length)) != -1) {
                idx = mask(context, idx);
            }
        });
    }

    private int indexEndOf(char[] arr, String c, int start, int end) {
        int last = Math.min(end, arr.length);
        for (int i = start; i < last; i++) {
            int j = 0;
            for (j = 0; j < c.length() && i+j < last; j++) {
                if (arr[i + j] != c.charAt(j) && (!ignoreCase || Character.toLowerCase(arr[i + j]) != c.charAt(j))) {
                    break;
                }
            }

            if (j == c.length()) {
                if (DEFAULT_SYMBOLS.contains(c) || DEFAULT_SYMBOLS.stream().anyMatch(c::endsWith)) {
                    return i + j;
                }
                int p = i + j;
                String s = DEFAULT_SYMBOLS.stream()
                        .filter(symbol -> indexEndOf(arr, symbol, p, p + symbol.length())
                            == p + symbol.length())
                        .findFirst().orElse(null);
                if(s != null) {
                    return p + s.length();
                }
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
