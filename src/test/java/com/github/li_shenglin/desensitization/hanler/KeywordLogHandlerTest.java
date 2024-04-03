package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;
import com.github.li_shenglin.desensitization.mask.PositionMask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author shenglin.li  2024/4/3 21:59
 * @version 1.0
 */
class KeywordLogHandlerTest {

    @Test
    void handler() {
        String[] keywords = {"pwd", "key"};
        KeywordLogHandler keywordLogHandler = new KeywordLogHandler(keywords, new PositionMask());

        MatchContext matchContext = new MatchContext("pwd=123456,key=123456");
        keywordLogHandler.handler(matchContext);
        assertEquals("pwd=******,key=******", new String(matchContext.getResult()));

        matchContext = new MatchContext("xl=xadpwd=123456,key=123456 xl=xad");
        keywordLogHandler.handler(matchContext);
        assertEquals("xl=xadpwd=******,key=****** xl=xad", new String(matchContext.getResult()));
    }

    @Test
    void handler1() {
        String[] keywords = {"pwd", "key"};
        KeywordLogHandler keywordLogHandler = new KeywordLogHandler(keywords, new Character[]{'6'}, new PositionMask());

        MatchContext matchContext = new MatchContext("pwd=123456,key=123456");
        keywordLogHandler.handler(matchContext);
        assertEquals("pwd=*****6,key=*****6", new String(matchContext.getResult()));

        matchContext = new MatchContext("xl=xadpwd=123456,key=123456 xl=xad");
        keywordLogHandler.handler(matchContext);
        assertEquals("xl=xadpwd=*****6,key=*****6 xl=xad", new String(matchContext.getResult()));
    }
}
