package com.ca.mfd.prc.common.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelConfigConversionAttribute {

    /**
     * 配置文件分类key
     */
    String configCategoryKey() default "";

}
