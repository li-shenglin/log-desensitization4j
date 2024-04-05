package io.github.li_shenglin.desensitization.hanler;

import io.github.li_shenglin.desensitization.core.MatchContext;
import io.github.li_shenglin.desensitization.mask.Mask;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JSON 格式日志处理  正则匹配， 不做严格处理
 *
 * @author shenglin.li  2024/4/4 0:34
 * @version 1.0
 */
public class JsonLogHandler extends AbsLogHandler {
    protected String[] keywords;
    protected int depth;

    protected final boolean ignoreCase;
    private RegexLogHandler handler;


    public JsonLogHandler(String[] keywords, Integer depth, Mask maskHandler) {
        this(keywords, depth, true, maskHandler);
    }

    public JsonLogHandler(String[] keywords, Integer depth, boolean ignoreCase, Mask maskHandler) {
        super(maskHandler);
        this.keywords = keywords;
        this.depth = Math.min(depth, 3);
        this.ignoreCase = ignoreCase;
        List<String> list = new ArrayList<>();
        for (String keyword : keywords) {
            if (ignoreCase) {
                keyword = keyword.toLowerCase()
                        .chars()
                        .mapToObj(c -> String.format("[%s%s]", (char)c, Character.toUpperCase((char)c)))
                        .collect(Collectors.joining());
            }
            list.add(MessageFormat
                    .format("\\\\{1}\"{0}\\\\{1}\":\\s*\\\\{1}\"?(.*?)\\\\{1}\"?[,}]",
                            keyword,
                            String.format("{0,%d}", this.depth <= 1 ? 0 : this.depth == 2 ? 1 : 3)));
        }
        handler = new RegexLogHandler(list.toArray(new String[0]), maskHandler);
    }


    @Override
    public void handler(MatchContext context) {
        handler.handler(context);
    }
}
