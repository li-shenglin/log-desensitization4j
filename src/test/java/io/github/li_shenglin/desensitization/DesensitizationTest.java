package io.github.li_shenglin.desensitization;

import io.github.li_shenglin.desensitization.core.ConfigReader;
import io.github.li_shenglin.desensitization.core.DesensitizationFactory;
import io.github.li_shenglin.desensitization.core.DesensitizationLogEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 整体单测
 *
 * @author shenglin.li  2024/4/4 15:31
 * @version 1.0
 */
public class DesensitizationTest {
    ConfigReader.DefaultConfigReader reader = new ConfigReader.DefaultConfigReader();
    static String log1 = "password=123456,secret=1234567890, context={\"password\": \"sadqweqweq\"}";
    static String log2 = "password=123456,secret=123456,tel=123456,phone:123456,email=123456@qq.com,publicKey=123456,password=123456,secret=123456,tel=123456,phone:123456,email=123456@qq.com,publicKey=123456";

    @Test
    void handler1() throws IOException {
        DesensitizationFactory.setReader(() -> reader.readConfig("/log-desensitization-test.yml"));
        DesensitizationFactory.reload();

        DesensitizationLogEvent event = new DesensitizationLogEvent("com.test1", log1);
        DesensitizationFactory.desensitization(event);
        assertEquals(event.getFormatMessage(), log1);

        event = new DesensitizationLogEvent("com.a", log1);
        DesensitizationFactory.desensitization(event);
        assertEquals(event.getFormatMessage(), "password=******,secret=123***7890, context={\"password\": \"**********\"}");

        event = new DesensitizationLogEvent("com.a", log2);
        DesensitizationFactory.desensitization(event);
        assertEquals(event.getFormatMessage(), log2);
    }

    @Test
    void handler2() throws IOException {
        DesensitizationFactory.setReader(() -> reader.readConfig("/log-desensitization-test1.yml"));
        DesensitizationFactory.reload();

        DesensitizationLogEvent event = new DesensitizationLogEvent("com.test1", log1);
        DesensitizationFactory.desensitization(event);
        assertEquals("password=******,secret=1********0, context={\"password\": \"sadqweqweq\"}", event.getFormatMessage());

        event = new DesensitizationLogEvent("com.a", log2);
        DesensitizationFactory.desensitization(event);
        assertEquals(event.getFormatMessage(), "password=******,secret=1****6,tel=1****6,phone:8d969e,email=123***@qq.com,publicKey=123456,password=******,secret=1****6,tel=1****6,phone:8d969e,email=123***@qq.com,publicKey=123456");
    }
}
