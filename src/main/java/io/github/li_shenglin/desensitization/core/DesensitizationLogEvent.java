package io.github.li_shenglin.desensitization.core;

import org.slf4j.helpers.MessageFormatter;

/**
 * 日志事件
 *
 * @author shenglin.li  2024/4/3 21:24
 * @version 1.0
 */
public class DesensitizationLogEvent {
    private String loggerName;
//    private String message;
//    private Object[] args;
    private String formatMessage;

//    public String getFormatMessage() {
//        return MessageFormatter.arrayFormat(message, args).getMessage();
//    }


    public DesensitizationLogEvent(String loggerName, String formatMessage) {
        this.loggerName = loggerName;
        this.formatMessage = formatMessage;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getFormatMessage() {
        return formatMessage;
    }

    public void setFormatMessage(String formatMessage) {
        this.formatMessage = formatMessage;
    }
}
