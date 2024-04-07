package io.github.li_shenglin.desensitization.springcloud;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用Springboot的上下文
 *
 * @author shenglin.li  2024/4/7 21:13
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PropertySource(value = {"classpath:log-desensitization-default.yml", "classpath:log-desensitization.yml"}, ignoreResourceNotFound = true,
encoding = "utf-8", factory = YamlPropertySourceFactory.class)
@EnableConfigurationProperties({SpringProperties.class})
@Import({ConfigInitRunner.class})
public @interface EnableSpringBootContext {
}
