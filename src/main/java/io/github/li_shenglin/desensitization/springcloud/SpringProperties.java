package io.github.li_shenglin.desensitization.springcloud;

import io.github.li_shenglin.desensitization.config.DesensitizationConfig;
import io.github.li_shenglin.desensitization.core.DesensitizationFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;

/**
 * 属性
 *
 * @author shenglin.li  2024/4/7 20:39
 * @version 1.0
 */
@RefreshScope
@ConfigurationProperties(prefix = "log-desensitization")
public class SpringProperties extends DesensitizationConfig {
    @EventListener
    public void handleEnvironmentChangeEvent(EnvironmentChangeEvent event) {
        DesensitizationFactory.reload();
    }
}
