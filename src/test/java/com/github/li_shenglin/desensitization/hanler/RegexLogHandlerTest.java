package com.github.li_shenglin.desensitization.hanler;

import com.github.li_shenglin.desensitization.core.MatchContext;
import com.github.li_shenglin.desensitization.mask.PositionMask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author shenglin.li  2024/4/3 23:34
 * @version 1.0
 */
class RegexLogHandlerTest {

    @Test
    void handler() {
        RegexLogHandler handler = new RegexLogHandler(new String[]{"pwd\\((.*?)-(.*?)\\)"}, new PositionMask());

        MatchContext matchContext = new MatchContext("[pwd(123456-abcde), pwd(123456-abcde)]key=123456");
        handler.handler(matchContext);
        assertEquals("[pwd(******-*****), pwd(******-*****)]key=123456", new String(matchContext.getResult()));

        matchContext = new MatchContext("xl=xadpwd(123456-acdefdadfsd),key=123456 xl=xad");
        handler.handler(matchContext);
        assertEquals("xl=xadpwd(******-***********),key=123456 xl=xad", new String(matchContext.getResult()));
    }

    @Test
    void handler1() {
        RegexLogHandler handler = new RegexLogHandler(new String[]{"pwd\\((.*?)-(.*?)\\)", "xl=x(.*?)d"}, new PositionMask());

        MatchContext matchContext = new MatchContext("[pwd(123456-abcde), pwd(123456-abcde)]key=123456");
        handler.handler(matchContext);
        assertEquals("[pwd(******-*****), pwd(******-*****)]key=123456", new String(matchContext.getResult()));

        matchContext = new MatchContext("xl=xadpwd(123456-acdefdadfsd),key=123456 xl=xad");
        handler.handler(matchContext);
        assertEquals("xl=x*dpwd(******-***********),key=123456 xl=x*d", new String(matchContext.getResult()));
    }
}