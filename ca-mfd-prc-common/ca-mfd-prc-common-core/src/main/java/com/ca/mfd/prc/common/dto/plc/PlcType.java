package com.ca.mfd.prc.common.dto.plc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PlcType
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PlcType {

    double Index() default 0;

    int ByteLength() default 0;
}
