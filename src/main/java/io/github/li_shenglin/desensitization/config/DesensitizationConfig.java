package io.github.li_shenglin.desensitization.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 脱敏配置
 *
 * @author shenglin.li  2024/4/3 15:17
 * @version 1.0
 */
public class DesensitizationConfig {
    private String[] baseLoggerPackage;

    private Integer   maxLength;

    private List<MatchConfig> matcher;

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String[] getBaseLoggerPackage() {
        if (baseLoggerPackage == null) {
            return new String[0];
        }
        return baseLoggerPackage;
    }

    public void setBaseLoggerPackage(String[] baseLoggerPackage) {
        this.baseLoggerPackage = baseLoggerPackage;
    }

    public List<MatchConfig> getMatcher() {
        if (matcher == null) {
            return new ArrayList<>();
        }
        return matcher;
    }

    public void setMatcher(List<MatchConfig> matcher) {
        this.matcher = matcher;
    }


}
