/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import com.ca.mfd.prc.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 字符串工具类
 *
 * @author eric.zhou
 */
public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 删除末尾字符串
     *
     * @param inStr
     * @param suffix
     * @return
     */
    public static String trimEnd(String inStr, String suffix) {
        if (inStr == null || suffix == null) {
            return inStr;
        }
        while (inStr.endsWith(suffix)) {
            inStr = inStr.substring(0, inStr.length() - suffix.length());
        }
        return inStr;
    }

    /**
     * 删除末尾字符串
     *
     * @param inStr
     * @param suffix
     * @return
     */
    public static String trimStart(String inStr, String suffix) {
        if (inStr == null || suffix == null) {
            return inStr;
        }
        while (inStr.startsWith(suffix)) {
            inStr = inStr.substring(suffix.length());
        }
        return inStr;
    }

    /**
     * 删除两端字符串
     *
     * @param inStr
     * @param suffix
     * @return
     */
    public static String trim(String inStr, String suffix) {
        if (inStr == null || suffix == null) {
            return inStr;
        }
        inStr = trimStart(inStr, suffix);
        inStr = trimEnd(inStr, suffix);
        return inStr;
    }

    /**
     * 判断long数据是否是空(null或者0)
     *
     * @param val
     * @return
     */
    public static boolean isLongEmpty(String val) {
        if (val == null || org.apache.commons.lang3.StringUtils.isBlank(val)) {
            return true;
        }
        if (String.valueOf(Constant.DEFAULT_ID).equals(val.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 判断long数据是否是空(null或者0)
     *
     * @param val
     * @return
     */
    public static boolean isLongEmpty(Long val) {
        if (val == null || Objects.equals(val, Constant.DEFAULT_ID)) {
            return true;
        }
        return false;
    }

    public static List<String> split(String src, char[] separatorChar){
        if(org.apache.commons.lang3.StringUtils.isBlank(src)
            || separatorChar == null || separatorChar.length == 0){
            return Collections.emptyList();
        }
        List<String> targetList = new ArrayList<>(16);
        targetList.addAll(split(src, separatorChar[0]));
        if(separatorChar.length > 1){
            for(char eachSeparator : separatorChar){
                List<String> temporaryList = split(targetList, eachSeparator);
                targetList.clear();
                targetList.addAll(temporaryList);
            }
        }
        return targetList;

    }

    public static List<String> split(String src, char separatorChar){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(src)){
            String[] strArray = org.apache.commons.lang3.StringUtils.split(src,separatorChar);
            List<String> targetList = new ArrayList<>(strArray.length);
            for(String item : strArray){
                if(org.apache.commons.lang3.StringUtils.isNotBlank(item)){
                    targetList.add(org.apache.commons.lang3.StringUtils.trim(item));
                }
            }
            return targetList;
        }
        return Collections.emptyList();
    }

    public static List<String> split(List<String> srcList, char separatorChar){
        if(srcList.isEmpty()){
            return srcList;
        }
        List<String> targetList = new ArrayList<>(srcList.size() * 2);
        for(String src : srcList){
            targetList.addAll(split(src,separatorChar));
        }
        return targetList;
    }

    /**
     * 根据整车物料号获取车型
     *
     * */
    public static String getSubStr(String str,int len) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * 根据整车物料号获取车型
     *
     * */
    public static String getModel(String productCode) {
        if (org.apache.commons.lang3.StringUtils.isBlank(productCode)) {
            return "";
        }
        String[] productCodes = productCode.split("\\.");
        return trimEnd(productCodes[0], ".");
    }

    public static void main(String[] args){
        String str = "ab|ddf;fdd|cd,1ad|abc;def|kfc,adf|sdfd;hahaha|dfddss";
        long begin = System.currentTimeMillis();
        List<String> target = split(str, new char[]{',', '|', ';'});
        long end = System.currentTimeMillis();
        System.out.println(end-begin);
        long begin1 = System.currentTimeMillis();
        List<String> target1 = ArraysUtils.splitNoEmpty(str, "[\\|\\,\\;]");
        long end1 = System.currentTimeMillis();
        System.out.println(end1-begin1);

    }

}