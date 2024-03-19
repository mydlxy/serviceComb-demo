package com.ca.mfd.prc.common.annotation;

import com.ca.mfd.prc.common.config.MybatisPlusConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EnableMybatisPlus interface
 *
 * @author cwy
 * @date 2023/3/24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MybatisPlusConfig.class})
public @interface EnableMybatisPlus {
}
