package io.github.li_shenglin.desensitization.core;

import io.github.li_shenglin.desensitization.config.DesensitizationConfig;
import io.github.li_shenglin.desensitization.config.MatchConfig;
import io.github.li_shenglin.desensitization.hanler.*;
import io.github.li_shenglin.desensitization.mask.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 脱敏工厂 组合组件 对外服务
 *
 * @author shenglin.li  2024/4/4 9:57
 * @version 1.0
 */
public class DesensitizationFactory {
    private List<String> baseLoggerPackage;

    private Integer maxLength;

    private List<LogHandler> handlers;

    private DesensitizationFactory(List<String> baseLoggerPackage, Integer maxLength, List<LogHandler> handlers) {
        this.baseLoggerPackage = baseLoggerPackage;
        this.maxLength = maxLength;
        this.handlers = handlers;
    }

    private void doDesensitization(DesensitizationLogEvent logEvent) {
        if (logEvent == null || logEvent.getLoggerName() == null || logEvent.getFormatMessage() == null) {
            return;
        }

        if (!baseLoggerPackage.isEmpty() && baseLoggerPackage.stream().noneMatch(e -> logEvent.getLoggerName().startsWith(e))) {
            return;
        }

        if (logEvent.getFormatMessage().length() > maxLength) {
            return;
        }
        MatchContext matchContext = new MatchContext(logEvent.getFormatMessage());
        handlers.forEach(e -> e.handler(matchContext));
        logEvent.setFormatMessage(matchContext.refresh());
    }

    private static volatile Function<String, Object> instanceFunc = name -> {
        try {
            Class<?> aClass = Class.forName(name);
            return aClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("can`t find class: " + name);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    };

    private static volatile ConfigReader reader = new ConfigReader.DefaultConfigReader();
    private static volatile DesensitizationFactory instance = null;

    public static DesensitizationFactory getInstance() {
        if (instance == null) {
            synchronized (DesensitizationFactory.class) {
                if (instance == null) {
                    try {
                        instance = buildDesensitizationFactory(reader.getConfig());
                    } catch (Exception e) {
                        System.out.println("DesensitizationFactory init error: " + e.getMessage());
                    }
                }
            }
        }
        return instance;
    }
    public static synchronized void reload() {
        synchronized (DesensitizationFactory.class) {
            try {
                instance = buildDesensitizationFactory(reader.getConfig());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setReader(ConfigReader reader) {
        DesensitizationFactory.reader = reader;
    }

    public static void setInstanceFunc(Function<String, Object> func) {
        DesensitizationFactory.instanceFunc = func;
    }

    public static void desensitization(DesensitizationLogEvent logEvent) {
        Optional.ofNullable(getInstance()).ifPresent(ins -> ins.doDesensitization(logEvent));
    }

    public static DesensitizationFactory buildDesensitizationFactory(DesensitizationConfig config) {
        String[] baseLoggerPackage = config.getBaseLoggerPackage();
        List<String> baseLoggerPackageList = Arrays.asList(baseLoggerPackage);
        baseLoggerPackageList.sort(Comparator.comparingInt(String::length));

        List<LogHandler> handlerList = config.getMatcher()
                .stream()
                .flatMap(e -> Stream.of(e.getType().split(",")).map(type -> buildLogHandler(type, e)))
                .collect(Collectors.toList());
        return new DesensitizationFactory(baseLoggerPackageList, config.getMaxLength(), handlerList);
    }

    private static LogHandler buildLogHandler(String type, MatchConfig config) {
        switch (type.trim()) {
            case "json":
                return new JsonLogHandler(config.getKeywords(), config.getDepth(), config.isIgnoreCase(), buildMask(config.getDesensitization()));
            case "regex":
                return new RegexLogHandler(config.getPattens(), buildMask(config.getDesensitization()));
            case "keyword":
                return new KeywordLogHandler(config.getKeywords(), config.getEndSymbols(), buildMask(config.getDesensitization()), config.isIgnoreCase());
            case "custom":
                Object apply = instanceFunc.apply(config.getClassName());
                if (apply instanceof LogHandler) {
                    return new CustomLogHandler((LogHandler) apply, buildMask(config.getDesensitization()));
                }
                throw new IllegalArgumentException("class " + config.getClassName() + " isn`t a LogHandler");
            default:
                throw new IllegalArgumentException("unknown type: " + type);
        }

    }

    private static Mask buildMask(String maskWord) {
        maskWord = maskWord.trim();
        if (maskWord.startsWith("hash")) {
            return HashMask.build(maskWord);
        } else if (maskWord.startsWith("fixed")) {
            return FixedMask.build(maskWord);
        } else if (maskWord.startsWith("mask")) {
            return PositionMask.build(maskWord);
        } else if (maskWord.startsWith("custom")) {
            if (maskWord.startsWith("custom(") && maskWord.endsWith(")")) {
                String className = maskWord.substring(7, maskWord.length() - 1);
                Object o = instanceFunc.apply(className);
                if (o instanceof Mask) {
                    return new CustomMask((Mask) o);
                }
                throw new RuntimeException("class: " + className + " isn`t a Mask instance");
            }
            throw new IllegalArgumentException("can`t parse desensitization[Custom]: " + maskWord);
        } else {
            throw new IllegalArgumentException("can`t parse desensitization: " + maskWord);
        }
    }
}
