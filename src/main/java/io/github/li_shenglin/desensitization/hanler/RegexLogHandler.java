package io.github.li_shenglin.desensitization.hanler;

import io.github.li_shenglin.desensitization.core.MatchContext;
import io.github.li_shenglin.desensitization.mask.Mask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则处理
 *
 * @author shenglin.li  2024/4/3 22:52
 * @version 1.0
 */
public class RegexLogHandler extends AbsLogHandler {
    protected List<Pattern> regex = new ArrayList<>();

    public RegexLogHandler(String[] pattens, Mask maskHandler) {
        super(maskHandler);
        for (String patten : pattens) {
            regex.add(Pattern.compile(patten));
        }
    }

    @Override
    public void handler(MatchContext context) {
        regex.forEach(p -> singleHandler(p, context));
    }

    private void singleHandler(Pattern pattern, MatchContext context) {
        String refresh = context.refresh();
        Matcher matcher = pattern.matcher(refresh);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                int start = matcher.start(i);
                int end = matcher.end(i);
                context.setResult(maskHandler.convert(context.getResult(), start, end));
            }

        }
    }
}
