/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换工具类
 *
 * @author inkelink
 */
public class FeatureTool {

    /**
     * 全车型
     */
    public static final String ALLMODELS = "ALLMODELS";
    /**
     * 全特征
     */
    public static final String ALLTYPES = "ALLTYPES";
    /**
     * 不同车型的分割符号
     */
    public static final String MODEL_SPLIT = ";";
    /**
     * 车型和特征的分割符号
     * 车型:特征
     * ALLMODELS:ALLTYPES
     */
    public static final String MODEL_FEATURE_SPLIT = ":";
    /**
     * 或符号
     */
    public static final String OR = "\\|";
    /**
     * 与符号
     */
    public static final String AND = "\\&";
    /**
     * 排除符号
     */
    public static final String EXCLUDE = "!";
    /**
     * 包含符号
     */
    public static final String INCLUDE = "#";

    private FeatureTool() {
    }

    /**
     * 特征验证
     *
     * @param expression           表达式
     * @param model                车型
     * @param characteristicValues 特征值
     * @return 是否符合特征表达式
     */
    public static Boolean calExpression(String expression, String model, List<String> characteristicValues) {
        // 特征值集合
        if (characteristicValues == null) {
            characteristicValues = new ArrayList<>();
        }
        // 为空直接返回false
        if (StringUtils.isBlank(expression)) {
            return false;
        }
        boolean result = false;
        // 去除表达式前后空格
        expression = expression.trim();
        // 去除括号
        expression = removeParentheses(expression);
        // 按照不同车型拆分成不同车型的特征表达式
        List<String> expressionList = ArraysUtils.splitNoEmpty(expression, MODEL_SPLIT);
        // 循环:将不同车型的表达式，拆分成车型，特征进行验证,根据传入的车型字段model进行验证和特征集合进行验证
        for (String oneExpression : expressionList) {
            result = splitModelAndFeatureToCal(model, characteristicValues, oneExpression);
        }
        return result;
    }

    /**
     * 先简单粗暴处理，后面再研究
     * @param expression
     * @return
     */
    private static String removeParentheses(String expression){
        return expression.replace("(","").replace(")","");
    }

    private static boolean splitModelAndFeatureToCal(String model, List<String> characteristicValues, String oneExpression) {
        boolean result = false;
        // 按照冒号拆分表达式 车型:特征
        List<String> splitItem = ArraysUtils.splitNoEmpty(oneExpression, MODEL_FEATURE_SPLIT);
        if (splitItem.size() != 2) {
            return false;
        }
        // 如果表达式匹配全车型或是匹配传入的车型值model，进入if
        if (StringUtils.equalsIgnoreCase(splitItem.get(0), ALLMODELS) || StringUtils.equalsIgnoreCase(splitItem.get(0), model)) {
            // 车辆特征
            String types = splitItem.get(1);
            // 如果表达式匹配全特征，直接通过，不进行下一步
            if (StringUtils.equalsIgnoreCase(types, ALLTYPES)) {
                result = true;
            } else {
                // 否则对车辆特征按照或符号拆分后验证
                result = orSymbolSplitToCal(characteristicValues, types);
            }
        }
        return result;
    }

    private static boolean orSymbolSplitToCal(List<String> characteristicValues, String parentExpression) {
        boolean result = false;
        // 按照或符号拆分
        List<String> splitByOrList = ArraysUtils.splitNoEmpty(parentExpression, OR);
        for (String expression : splitByOrList) {
            // 按照与条件条件进一步拆分和验证
            if (andSymbolSplitToCal(characteristicValues, expression)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean andSymbolSplitToCal(List<String> characteristicValues, String parentExpression) {
        // 按照与符号拆分
        List<String> splitByAndList = ArraysUtils.splitNoEmpty(parentExpression, AND);
        boolean flag = true;
        for (String expression : splitByAndList) {
            //最终验证
            if (!finalCalByExcludeOrInclude(characteristicValues, expression)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static boolean finalCalByExcludeOrInclude(List<String> characteristicValues, String parentExpression) {
        boolean flag = true;
        // 如果特征式要排除
        if (StringUtils.endsWith(parentExpression, EXCLUDE)) {
            // 先去掉特征值后面的排除符号
            String expression = com.ca.mfd.prc.common.utils.StringUtils.trimEnd(parentExpression, EXCLUDE);
            //如果传入的特征值集合包含了要排除的特征，验证不通过
            if (characteristicValues.contains(expression)) {
                flag = false;
            }
        } else {
            // 如果要包含的情况，
            String expression = com.ca.mfd.prc.common.utils.StringUtils.trimEnd(parentExpression, INCLUDE);
            // 如果传入的特征值集合不包含要包含的特征，验证不通过
            if (!characteristicValues.contains(expression)) {
                flag = false;
            }
        }
        return flag;
    }


    /**
     * 表达式格式验证
     *
     * @param expression 表达式值
     * @return 是否通过验证
     */
    public static Boolean verifyExpression(String expression) {
        if (StringUtils.isBlank(expression)) {
            return false;
        }

        // 去除表达式前后空格
        expression = expression.trim();
        // 按照不同车型拆分成不同车型的特征表达式
        List<String> expressionList = ArraysUtils.splitNoEmpty(expression, MODEL_SPLIT);
        // 循环验证每个车型的特征表达式是否符合格式要求
        for (String oneExpression : expressionList) {
            if (!splitModelAndFeatureToVerify(oneExpression)) {
                return false;
            }
        }
        return true;
    }

    private static boolean splitModelAndFeatureToVerify(String oneExpression) {
        // 按冒号拆分表达式，分为车型和特征两部分
        List<String> splitItem = ArraysUtils.splitNoEmpty(oneExpression, MODEL_FEATURE_SPLIT);
        // 如果车型部分为全车型或为空，则验证特征部分是否为全特征或符合格式要求
        if (splitItem.size() == 2 && (StringUtils.equalsIgnoreCase(splitItem.get(0), ALLMODELS)
                || !splitItem.get(0).isEmpty())) {
            // 特征部分
            String expression = splitItem.get(1);
            return StringUtils.equalsIgnoreCase(expression, ALLTYPES) || orSymbolSplitToVerify(expression);
        }
        return false;
    }

    private static boolean orSymbolSplitToVerify(String parentExpression) {
        // 按加号拆分特征表达式，分为多个条件
        List<String> splitByOrList = ArraysUtils.splitNoEmpty(parentExpression, OR);
        // 验证每个条件是否符合格式要求
        for (String expression : splitByOrList) {
            if (!andSymbolSplitToVerify(expression)) {
                return false;
            }
        }
        return true;
    }

    private static boolean andSymbolSplitToVerify(String parentExpression) {
        // 按星号拆分条件，分为多个子条件
        List<String> splitByAndList = ArraysUtils.splitNoEmpty(parentExpression, AND);
        // 验证每个子条件是否符合格式要求
        for (String expression : splitByAndList) {
            if (!finalVerifyByExcludeOrInclude(expression)) {
                return false;
            }
        }
        return true;
    }

    private static boolean finalVerifyByExcludeOrInclude(String parentExpression) {
        // 如果条件以排除符号结尾，则去除符号后验证是否为空
        if (StringUtils.endsWith(parentExpression, EXCLUDE)) {
            String expression = StringUtils.removeEnd(parentExpression, EXCLUDE);
            return !expression.isEmpty();
        }
        //如果条件以包含符号结尾，则去除符号后验证是否为空
        else if (StringUtils.endsWith(parentExpression, INCLUDE)) {
            String expression = StringUtils.removeEnd(parentExpression, INCLUDE);
            return !expression.isEmpty();
        }
        return true;
    }

    public  static void main(String[]  args){
        String str = "CD701EV:AAL001";
        System.out.println(verifyExpression(str));
        System.out.println(str);
    }

}