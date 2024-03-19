/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 校验工具类
 *
 * @author inkelink
 * @since 1.0.0
 */
public class AssertUtils {

    private AssertUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void isBlank(String str, String... params) {
        isBlank(str, ErrorCode.NOT_NULL, params);
    }

    public static void isBlank(String str, Integer code, String... params) {
        if (code == null) {
            throw new InkelinkException(ErrorCode.NOT_NULL, "code");
        }

        if (StringUtils.isBlank(str)) {
            throw new InkelinkException(code, params);
        }
    }

    public static void isNull(Object object, String... params) {
        isNull(object, ErrorCode.NOT_NULL, params);
    }

    public static void isNull(Object object, Integer code, String... params) {
        if (code == null) {
            throw new InkelinkException(ErrorCode.NOT_NULL, "code");
        }

        if (object == null) {
            throw new InkelinkException(code, params);
        }
    }

    public static void isArrayEmpty(Object[] array, String... params) {
        isArrayEmpty(array, ErrorCode.NOT_NULL, params);
    }

    public static void isArrayEmpty(Object[] array, Integer code, String... params) {
        if (code == null) {
            throw new InkelinkException(ErrorCode.NOT_NULL, "code");
        }

        if (ArrayUtil.isEmpty(array)) {
            throw new InkelinkException(code, params);
        }
    }

    public static void isListEmpty(List<?> list, String... params) {
        isListEmpty(list, ErrorCode.NOT_NULL, params);
    }

    public static void isListEmpty(List<?> list, Integer code, String... params) {
        if (code == null) {
            throw new InkelinkException(ErrorCode.NOT_NULL, "code");
        }

        if (CollUtil.isEmpty(list)) {
            throw new InkelinkException(code, params);
        }
    }

    public static void isMapEmpty(Map<? extends Serializable,? extends Serializable> map, String... params) {
        isMapEmpty(map, ErrorCode.NOT_NULL, params);
    }

    public static void isMapEmpty(Map<? extends Serializable,? extends Serializable> map, Integer code, String... params) {
        if (code == null) {
            throw new InkelinkException(ErrorCode.NOT_NULL, "code");
        }

        if (MapUtil.isEmpty(map)) {
            throw new InkelinkException(code, params);
        }
    }

//    public static void error() {
//        throw new InkelinkException(HttpCode._400_BAD_REQUEST.getMsg(), HttpCode._400_BAD_REQUEST);
//    }
//
//    public static void error(HttpCode httpCode) {
//        throw new InkelinkException(httpCode.getMsg(), httpCode);
//    }
//
//    public static void error(String message) {
//        throw new InkelinkException(message, HttpCode._400_BAD_REQUEST);
//    }
//
//    public static void error(HttpCode httpCode, String message) {
//        throw new InkelinkException(message, httpCode);
//    }

}