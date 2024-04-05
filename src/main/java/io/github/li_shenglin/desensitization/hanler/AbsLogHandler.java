package io.github.li_shenglin.desensitization.hanler;

import io.github.li_shenglin.desensitization.mask.Mask;

/**
 * @author shenglin.li  2024/4/3 22:59
 * @version 1.0
 */
public abstract class AbsLogHandler implements LogHandler{
    protected final Mask maskHandler;

    protected AbsLogHandler(Mask maskHandler) {
        this.maskHandler = maskHandler;
    }
}
