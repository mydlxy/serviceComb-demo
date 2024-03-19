package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.pps.dto.CheckFetureExpressionPara;
import com.ca.mfd.prc.pps.dto.FetureTestPara;
import com.ca.mfd.prc.pps.dto.FilterFetureExpressionPara;

import java.util.List;

/**
 * PpsLogic
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IAnalysisFetureService {

    /**
     * 过滤命中的特征表达式
     *
     * @param para 需要过滤的特征表达式集合
     * @return 过滤后的特征表达式集合
     */
    List<String> filterFetureExpression(FilterFetureExpressionPara para);

    /**
     * 检查特征表达式是否命中
     *
     * @param para 特征表达式
     * @return true合法，false不合法
     */
    Boolean checkFetureExpression(CheckFetureExpressionPara para);

    /**
     * 验证特征表达式
     *
     * @param para 特征表达式
     * @return true合法，false不合法
     */
    Boolean verifyFetureExpression(FetureTestPara para);

}
