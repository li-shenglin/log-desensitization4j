package io.github.li_shenglin.desensitization.hanler;

import io.github.li_shenglin.desensitization.core.MatchContext;
import io.github.li_shenglin.desensitization.mask.Mask;

import java.util.Objects;

/**
 * 自定义过滤
 *
 * @author shenglin.li  2024/4/5 8:25
 * @version 1.0
 */
public class CustomLogHandler extends AbsLogHandler {
    LogHandler handler;
    public CustomLogHandler(LogHandler handler, Mask maskHandler) {
        super(maskHandler);
        this.handler = Objects.requireNonNull(handler);
    }
    @Override
    public void handler(MatchContext context) {
        handler.handler(context);
    }
}
