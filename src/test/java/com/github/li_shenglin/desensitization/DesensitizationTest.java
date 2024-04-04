package com.github.li_shenglin.desensitization;

import com.github.li_shenglin.desensitization.config.DesensitizationConfig;
import com.github.li_shenglin.desensitization.core.ConfigReader;
import com.github.li_shenglin.desensitization.core.DesensitizationBuilder;
import com.github.li_shenglin.desensitization.core.DesensitizationFactory;
import com.github.li_shenglin.desensitization.core.DesensitizationLogEvent;
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
    static String log1 = "password=123456,secret=1234567890";
    static String log2 = "password=123456,secret=123456,tel=123456,phone:123456,email=123456@qq.com,publicKey=123456,password=123456,secret=123456,tel=123456,phone:123456,email=123456@qq.com,publicKey=123456";
    @Test
    void handler1() throws IOException {
        DesensitizationConfig desensitizationConfig = reader.readConfig("/log-desensitization-test.yml");

        DesensitizationFactory factory = DesensitizationBuilder.buildDesensitizationFactory(desensitizationConfig);

        String result = factory.desensitization(new DesensitizationLogEvent("com.test1", log1));
        assertEquals(result, log1);

        result = factory.desensitization(new DesensitizationLogEvent("com.a", log1));
        assertEquals(result, "password=******,secret=123***7890");

        result = factory.desensitization(new DesensitizationLogEvent("com.a", log2));
        assertEquals(result, log2);
    }

    @Test
    void handler2() throws IOException {
        DesensitizationConfig desensitizationConfig = reader.readConfig("/log-desensitization-test1.yml");

        DesensitizationFactory factory = DesensitizationBuilder.buildDesensitizationFactory(desensitizationConfig);

        String result = factory.desensitization(new DesensitizationLogEvent("com.test1", log1));
        assertEquals("password=******,secret=1********0", result);

        result = factory.desensitization(new DesensitizationLogEvent("com.a", log2));
        assertEquals(result, "password=******,secret=1****6,tel=1****6,phone:8d969e,email=123***@qq.com,publickey=123456,password=******,secret=1****6,tel=1****6,phone:8d969e,email=123***@qq.com,publickey=123456");
    }
}
