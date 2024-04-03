package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;

/**
 * 日志匹配
 *
 * @author shenglin.li  2024/4/3 01:42
 * @version 1.0
 */
public interface LogHandler {
    void handler(MatchContext context);
}
