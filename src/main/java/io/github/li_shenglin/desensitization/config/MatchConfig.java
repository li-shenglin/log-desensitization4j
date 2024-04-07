package io.github.li_shenglin.desensitization.config;

/**
 * @author shenglin.li  2024/4/3 16:57
 * @version 1.0
 */
public class MatchConfig {
    private String type;

    private String[] keywords;
    private String[] pattens;
    private Integer depth = 1;
    private String desensitization = "mask";

    private String className;

    private boolean ignoreCase = true;

    private Character[] endSymbols;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String[] getPattens() {
        return pattens;
    }

    public void setPattens(String[] pattens) {
        this.pattens = pattens;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getDesensitization() {
        return desensitization;
    }

    public void setDesensitization(String desensitization) {
        this.desensitization = desensitization;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public Character[] getEndSymbols() {
        return endSymbols;
    }

    public void setEndSymbols(Character[] endSymbols) {
        this.endSymbols = endSymbols;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
