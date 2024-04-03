package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;
import com.github.li_shenglin.desensitization.mask.Mask;
import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 格式日志处理  正则匹配， 不做严格处理
 *
 * @author shenglin.li  2024/4/4 0:34
 * @version 1.0
 */
public class JsonLogHandler extends AbsLogHandler {
    protected String[] keywords;
    protected int depth;

    private RegexLogHandler handler;


    public JsonLogHandler(String[] keywords, Integer depth, Mask maskHandler) {
        super(maskHandler);
        this.keywords = keywords;
        this.depth = Math.min(depth, 5);
        List<String> list = new ArrayList<>();
        for (String keyword : keywords) {
            list.add(MessageFormat
                    .format("\\\\{1}\"{0}\\\\{1}\":\\s*\\\\{1}\"?(.*?)\\\\{1}\"?[,}]",
                            keyword, String.format("{0,%d}", this.depth)));
        }
        handler = new RegexLogHandler(list.toArray(new String[0]), maskHandler);
    }


    @Override
    public void handler(MatchContext context) {
        handler.handler(context);
    }
}
