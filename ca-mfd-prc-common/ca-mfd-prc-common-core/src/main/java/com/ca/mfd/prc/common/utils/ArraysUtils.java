/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数组工具类
 *
 * @author eric.zhou
 */
public class ArraysUtils {

    private ArraysUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 分割字符（去掉空格）
     */
    public static List<String> splitNoEmpty(String source, String splitstr) {
        if (StringUtils.isBlank(source)) {
            return new ArrayList<>();
        }
        List<String> tmp = Arrays.asList(source.split(splitstr));
        return tmp.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

}