package io.github.li_shenglin.desensitization.hanler;

import io.github.li_shenglin.desensitization.core.MatchContext;
import io.github.li_shenglin.desensitization.mask.FixedMask;
import io.github.li_shenglin.desensitization.mask.PositionMask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author shenglin.li  2024/4/18 17:26
 * @version 1.0
 */
class ArrLogHandlerTest {

    @Test
    void handler() {
        String text = "{\"addresses\":[\"asdasa\",\"asdasdasdsasd\"]}";

        ArrLogHandler handler = new ArrLogHandler(new String[]{"addresses"}, 3, new FixedMask("*"));
        MatchContext context = new MatchContext(text);
        handler.handler(context);

        assertEquals("{\"addresses\":[*]}",
                context.refresh());
    }
}
