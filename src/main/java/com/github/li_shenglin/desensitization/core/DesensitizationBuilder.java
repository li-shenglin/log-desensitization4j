package com.github.li_shenglin.desensitization.core;

import com.github.li_shenglin.desensitization.config.DesensitizationConfig;
import com.github.li_shenglin.desensitization.config.MatchConfig;
import com.github.li_shenglin.desensitization.hanler.JsonLogHandler;
import com.github.li_shenglin.desensitization.hanler.KeywordLogHandler;
import com.github.li_shenglin.desensitization.hanler.LogHandler;
import com.github.li_shenglin.desensitization.hanler.RegexLogHandler;
import com.github.li_shenglin.desensitization.mask.FixedMask;
import com.github.li_shenglin.desensitization.mask.HashMask;
import com.github.li_shenglin.desensitization.mask.Mask;
import com.github.li_shenglin.desensitization.mask.PositionMask;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 脱敏
 *
 * @author shenglin.li  2024/4/3 16:53
 * @version 1.0
 */
public class DesensitizationBuilder {
    private DesensitizationBuilder() {}
    public static DesensitizationFactory buildDesensitizationFactory(DesensitizationConfig config) {
        String[] baseLoggerPackage = config.getBaseLoggerPackage();
        List<String> baseLoggerPackageList = Arrays.asList(baseLoggerPackage);
        baseLoggerPackageList.sort(Comparator.comparingInt(String::length));

        List<LogHandler> handlerList = config.getMatcher()
                .stream()
                .map(DesensitizationBuilder::buildLogHandler)
                .collect(Collectors.toList());
        return new DesensitizationFactory(baseLoggerPackageList, config.getMaxLength(), handlerList);
    }

    private static LogHandler buildLogHandler(MatchConfig config) {
        switch (config.getType().trim()) {
            case "json":
                return new JsonLogHandler(config.getKeywords(), config.getDepth(), config.isIgnoreCase(), buildMask(config.getDesensitization()));
            case "regex":
                return new RegexLogHandler(config.getPattens(), buildMask(config.getDesensitization()));
            case "keyword":
                return new KeywordLogHandler(config.getKeywords(), config.getEndSymbols(), buildMask(config.getDesensitization()), config.isIgnoreCase());
            default:
                throw new IllegalArgumentException("unknown type: " + config.getType());
        }

    }

    private static Mask buildMask(String maskWord) {
        maskWord = maskWord.trim();
        if(maskWord.startsWith("hash")) {
            return HashMask.build(maskWord);
        }else if (maskWord.startsWith("fixed")){
            return FixedMask.build(maskWord);
        }else if (maskWord.startsWith("mask")){
            return PositionMask.build(maskWord);
        } else {
            throw new IllegalArgumentException("can`t parse desensitization: " + maskWord);
        }
    }
}
