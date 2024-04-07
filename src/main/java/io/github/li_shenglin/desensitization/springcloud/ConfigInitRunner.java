package io.github.li_shenglin.desensitization.springcloud;

import io.github.li_shenglin.desensitization.core.DesensitizationFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

/**
 * 配置初始化
 *
 * @author shenglin.li  2024/4/7 20:16
 * @version 1.0
 */
public class ConfigInitRunner implements CommandLineRunner  {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private SpringProperties springProperties;

    @Override
    public void run(String... args) throws Exception {
        DesensitizationFactory.setReader(() -> springProperties);
        DesensitizationFactory.setInstanceFunc(name -> {
            try {
                return applicationContext.getBean(Class.forName(name));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
