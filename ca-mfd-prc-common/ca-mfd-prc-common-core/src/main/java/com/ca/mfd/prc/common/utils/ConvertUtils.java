/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * 转换工具类
 *
 * @author inkelink
 */
public class ConvertUtils {

    private ConvertUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);
    private static final int LONG_BYTES_LEN = 8;
    private static final String BOOLEAN_TRUE_CHAR = "true";
    private static final String BOOLEAN_TRUE_NUM = "1";

    @SuppressWarnings("unused")
    public static <T> T sourceToTarget(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject);
        } catch (Exception e) {
            logger.error("convert error ", e);
        }

        return targetObject;
    }

    public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target) {
        if (sourceList == null) {
            return Collections.emptyList();
        }

        List<T> targetList = new ArrayList<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T targetObject = target.newInstance();
                BeanUtils.copyProperties(source, targetObject);
                targetList.add(targetObject);
            }
        } catch (Exception e) {
            logger.error("convert error ", e);
        }

        return targetList;
    }

    /**
     * 将map转换为对象,必须保证属性名称相同
     *
     * @return obj
     */
    public static Object mapToObject(Map<Object, Object> map, Class<?> clzz) {

        try {
            Object target = clzz.newInstance();
            if (CollectionUtils.isEmpty(map)) {
                return target;
            }
            Field[] fields = clzz.getDeclaredFields();
            if (!CollectionUtils.isEmpty(Arrays.asList(fields))) {
                Arrays.stream(fields).filter((Field field) -> map.containsKey(field.getName())).forEach(field -> {
                    //获取属性的修饰符
                    int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                        //在lambada中结束本次循环是用return,它不支持continue和break
                        return;
                    }
                    setFieldValueByType(map, field, target);
                });
            }
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private static void setFieldValueByType(Map<Object, Object> map, Field field, Object target) {
        try {
            //设置权限
            ReflectionUtils.makeAccessible(field);
            String longStr = "java.lang.Long";
            String integerStr = "java.lang.Integer";
            String trueStr = "true";
            String falseStr = "false";
            if (longStr.equals(field.getType().getName())) {
                ReflectionUtils.setField(field, target, Long.valueOf(map.getOrDefault(field.getName(), "0").toString()));
            } else if (integerStr.equals(field.getType().getName())) {
                String values = map.getOrDefault(field.getName(), "0").toString();
                if (trueStr.equalsIgnoreCase(values)) {
                    ReflectionUtils.setField(field, target, 1);
                } else if (falseStr.equalsIgnoreCase(values)) {
                    ReflectionUtils.setField(field, target, 0);
                } else {
                    ReflectionUtils.setField(field, target, Integer.valueOf(map.getOrDefault(field.getName(), "0").toString()));
                }
            } else {
                ReflectionUtils.setField(field, target, map.get(field.getName()));
            }
        } catch (Exception exception) {
            throw new InkelinkException(exception.getMessage());
        }
    }


    /**
     * long转换为byte
     *
     * @return 判断结果
     */
    public static byte[] getLongBytes(long data) {
        return new byte[]{(byte) ((int) (data & 255L)), (byte) ((int) (data >> 8 & 255L)), (byte) ((int) (data >> 16 & 255L)), (byte) ((int) (data >> 24 & 255L)), (byte) ((int) (data >> 32 & 255L)), (byte) ((int) (data >> 40 & 255L)), (byte) ((int) (data >> 48 & 255L)), (byte) ((int) (data >> 56 & 255L))};
    }


    /**
     * 生成随机数的byte
     *
     * @return byte
     */
    @SuppressWarnings("unused")
    public static byte[] longToBytes(long values) {
        byte[] buffer = new byte[LONG_BYTES_LEN];
        for (int i = 0; i < LONG_BYTES_LEN; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    /**
     * 转换long
     *
     * @param id id str
     * @return String id to Long
     */
    public static Long stringToLong(String id) {
        if (StringUtils.isBlank(id) || UUIDUtils.isGuidEmpty(id)) {
            return Constant.DEFAULT_ID;
        }
        try {
            return Long.valueOf(id.trim());
        } catch (Exception e) {
            logger.error(id + "转换long失败", e);
        }
        return Constant.DEFAULT_ID;
    }

    /**
     * 转换long
     *
     * @param ids ids
     * @return results
     */
    public static List<Long> stringToLongs(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> results = new ArrayList<>();
        for (String id : ids) {
            results.add(stringToLong(id));
        }
        return results;
    }

    /**
     * 转换Boolean
     *
     * @param val val
     * @return bool
     */
    public static Boolean stringToBoolean(String val, Boolean defaultVal) {
        if (StringUtils.isBlank(val)) {
            return defaultVal;
        }
        return BOOLEAN_TRUE_CHAR.equalsIgnoreCase(val.trim()) || BOOLEAN_TRUE_NUM.equalsIgnoreCase(val.trim());
    }

    /**
     * 字节转换16位int
     *
     * @param bytes
     * @param index
     * @return short
     */
    public static short toInt16(byte[] bytes, int index) {
        return (short) (255 & bytes[0 + index] | '\uff00' & bytes[1 + index] << 8);
    }

    /**
     * 字节转换16位int
     *
     * @param bytes
     * @param index
     * @return short
     */
    public static int toInt32(byte[] bytes, int index) {
        return 255 & bytes[0 + index] | '\uff00' & bytes[1 + index] << 8 | 16711680 & bytes[2 + index] << 16 | -16777216 & bytes[3 + index] << 24;
    }

    /**
     * 字节转换16位int
     *
     * @param bytes
     * @param index
     * @return short
     */
    public static long getLong(byte[] bytes, int index) {
        return 255L & (long) bytes[0 + index] | 65280L & (long) bytes[1 + index] << 8 | 16711680L & (long) bytes[2 + index] << 16 | 4278190080L & (long) bytes[3 + index] << 24 | 1095216660480L & (long) bytes[4 + index] << 32 | 280375465082880L & (long) bytes[5 + index] << 40 | 71776119061217280L & (long) bytes[6 + index] << 48 | -72057594037927936L & (long) bytes[7 + index] << 56;
    }

    /**
     * 将二进制字符串转换为字节数组
     *
     * @param binaryString
     * @return
     */
    public static byte[] binaryStringToByteArray(String binaryString) {
        // 计算字节数组的长度（每8个二进制位对应一个字节）
        int numOfBytes = binaryString.length() / 8;
        // 创建字节数组
        byte[] byteArray = new byte[numOfBytes];
        // 遍历二进制字符串的每8个字符，将其转换为一个字节并存储在字节数组中
        for (int i = 0; i < numOfBytes; i++) {
            // 从二进制字符串中提取8个字符作为一个字节的二进制表示
            String byteString = binaryString.substring(i * 8, i * 8 + 8);
            // 将字节的二进制表示转换为一个字节，并存储在字节数组中
            //byteArray[i] = (byte)Integer.parseInt(byteString, 2);
            byteArray[i] = getByteByStr(byteString);
        }
        // 返回字节数组
        return byteArray;
    }

    /**
     * 将二进制字符串转换为字节
     *
     * @param binaryString
     * @return
     */
    public static byte getByteByStr(String binaryString) {
        byte byteValue = 0;
        for (int i = 0; i < binaryString.length(); i++) {
            char c = binaryString.charAt(i);
            byteValue <<= 1;  // 左移一位
            if (c == '1') {
                byteValue |= 1;  // 设置最低位为 1
            }
        }
        return byteValue;
    }

    public static Double getDouble(String inStr,Double defval) {
        if (StringUtils.isBlank(inStr)) {
            return defval;
        }
        try {
            return Double.parseDouble(inStr);
        } catch (NumberFormatException ignored) {
            return defval;
        }
    }

    public static BigDecimal getDecimal(String inStr,BigDecimal defval) {
        if (StringUtils.isBlank(inStr)) {
            return defval;
        }
        try {
            return BigDecimal.valueOf(Double.parseDouble(inStr));
        } catch (NumberFormatException ignored) {
            return defval;
        }
    }

    public static Integer tryParse(String inStr) {
        if (StringUtils.isBlank(inStr)) {
            return null;
        }
        try {
            return parseInt(inStr);
        } catch (NumberFormatException ignored) {
            return null;
        }

    }
}