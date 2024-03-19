package com.ca.mfd.prc.common.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ExcelImprotAttribute
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelImprotAttribute {

    /**
     * 配置文件分类key
     */
    String configCategoryKey() default "";

    /**
     * 获取config配置文件的值
     */
    String errorMessage() default "";

    /**
     * 链接对象
     */
    String lnkedObject() default "";

}
