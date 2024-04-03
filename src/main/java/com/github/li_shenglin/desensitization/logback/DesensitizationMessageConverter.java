package com.github.li_shenglin.desensitization.logback;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 敏感日志脱敏
 * @author shenglin.li  2024/4/2 15:04
 * @version 1.0
 */
public class DesensitizationMessageConverter extends MessageConverter {
    @Override
    public String convert(ILoggingEvent event) {

        return "";
    }
}
