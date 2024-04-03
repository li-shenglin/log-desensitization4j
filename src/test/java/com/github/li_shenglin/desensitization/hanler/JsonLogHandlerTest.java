package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;
import com.github.li_shenglin.desensitization.mask.PositionMask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author shenglin.li  2024/4/4 1:08
 * @version 1.0
 */
class JsonLogHandlerTest {

    @Test
    void handler1() {
        String text = "entity={\"next\":{\"next\":{\"next\":{\"next\":{\"number\": 123456,\"r\":true,\"pwd\":\"abcde\"},\"number\":123456,\"r\":true,\"pwd\":\"abcde\"},\"number\":123456,\"r\":true,\"pwd\":\"abcde\"},\"number\":123456,\"r\":true,\"pwd\":\"abcde\"},\"number\":123456,\"r\":true,\"pwd\":\"abcde\"}";

        JsonLogHandler handler = new JsonLogHandler(new String[]{"number", "pwd"}, 1, new PositionMask());
        MatchContext context = new MatchContext(text);
        handler.handler(context);

        assertEquals("entity={\"next\":{\"next\":{\"next\":{\"next\":{\"number\": ******,\"r\":true,\"pwd\":\"*****\"},\"number\":******,\"r\":true,\"pwd\":\"*****\"},\"number\":******,\"r\":true,\"pwd\":\"*****\"},\"number\":******,\"r\":true,\"pwd\":\"*****\"},\"number\":******,\"r\":true,\"pwd\":\"*****\"}",
                context.refresh());
    }

    @Test
    void handler5() {
        String text = "{\"next\":\"{\\\"next\\\":\\\"{\\\\\\\"number\\\\\\\":123456,\\\\\\\"r\\\\\\\":true,\\\\\\\"pwd\\\\\\\":\\\\\\\"abcde\\\\\\\"}\\\",\\\"number\\\":123456,\\\"r\\\":true,\\\"pwd\\\":\\\"abcde\\\"}\",\"number\":123456,\"r\":true,\"pwd\":\"abcde\"}";

        JsonLogHandler handler = new JsonLogHandler(new String[]{"number", "pwd"}, 3, new PositionMask());
        MatchContext context = new MatchContext(text);
        handler.handler(context);

        assertEquals("{\"next\":\"{\\\"next\\\":\\\"{\\\\\\\"number\\\\\\\":******,\\\\\\\"r\\\\\\\":true,\\\\\\\"pwd\\\\\\\":\\\\\\\"*****\\\\\\\"}\\\",\\\"number\\\":******,\\\"r\\\":true,\\\"pwd\\\":\\\"*****\\\"}\",\"number\":******,\"r\":true,\"pwd\":\"*****\"}",
                context.refresh());
    }
}