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
        String text = "{\"addresses\":[\"0x8fedde00233031f4fb9a06f68ab083a20ee0588d\",\"0x8fedde00233031f4fb9a06f68ab083a20ee0588d\"]}";

        ArrLogHandler handler = new ArrLogHandler(new String[]{"addresses"}, 3, new FixedMask("*"));
        MatchContext context = new MatchContext(text);
        handler.handler(context);

        assertEquals("{\"addresses\":[*]}",
                context.refresh());
    }
}
