package com.github.li_shenglin.desensitization.core;

import com.github.li_shenglin.desensitization.hanler.LogHandler;

import java.util.List;

/**
 * 脱敏工厂 组合组件 对外服务
 *
 * @author shenglin.li  2024/4/4 9:57
 * @version 1.0
 */
public class DesensitizationFactory {
    private List<String> baseLoggerPackage;

    private Integer   maxLength;

    private List<LogHandler> handlers;

    public DesensitizationFactory(List<String> baseLoggerPackage, Integer maxLength, List<LogHandler> handlers) {
        this.baseLoggerPackage = baseLoggerPackage;
        this.maxLength = maxLength;
        this.handlers = handlers;
    }

    public String desensitization(DesensitizationLogEvent logEvent) {
        if (logEvent == null || logEvent.getLoggerName() == null || logEvent.getFormatMessage() == null) {
            return null;
        }

        if (!baseLoggerPackage.isEmpty() && baseLoggerPackage.stream().noneMatch(e -> logEvent.getLoggerName().startsWith(e))) {
            return logEvent.getFormatMessage();
        }

        if (logEvent.getFormatMessage().length() > maxLength) {
            return logEvent.getFormatMessage();
        }
        MatchContext matchContext = new MatchContext(logEvent.getFormatMessage());
        handlers.forEach(e -> e.handler(matchContext));
        return matchContext.refresh();
    }
}
