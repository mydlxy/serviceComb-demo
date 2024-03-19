package com.ca.mfd.prc.pps.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.dto.CheckFetureExpressionPara;
import com.ca.mfd.prc.pps.dto.FetureTestPara;
import com.ca.mfd.prc.pps.dto.FilterFetureExpressionPara;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductCharacteristicsVersionsProvider;
import com.ca.mfd.prc.pps.service.IAnalysisFetureService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 计划逻辑处理
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class AnalysisFetureServiceImpl implements IAnalysisFetureService {

    @Autowired
    private IPpsOrderService ppsOrderService;
    @Autowired
    private PmProductCharacteristicsVersionsProvider pmProductCharacteristicsVersionsProvider;


    /**
     * 过滤命中的特征表达式
     *
     * @param para 需要过滤的特征表达式集合
     * @return 过滤后的特征表达式集合
     */
    @Override
    public List<String> filterFetureExpression(FilterFetureExpressionPara para) {
        PpsOrderEntity orderInfo = ppsOrderService.getPpsOrderInfoByKey(para.getBarcode());
        if (orderInfo == null) {
            throw new InkelinkException("无效的产品条码【" + para.getBarcode() + "】");
        }

        List<String> characteristicsValues = pmProductCharacteristicsVersionsProvider.getCharacteristicsData(orderInfo.getProductCode()
                        , orderInfo.getCharacteristicVersion()).stream().map(PmProductCharacteristicsEntity::getProductCharacteristicsValue)
                .collect(Collectors.toList());

        List<String> result = new ArrayList<>();
        for (String expression : para.getFetureExpressions()) {
            if (FeatureTool.calExpression(expression, orderInfo.getModel(), characteristicsValues)) {
                result.add(expression);
            }
        }
        return result;
    }

    /**
     * 检查特征表达式是否命中
     *
     * @param para 特征表达式
     * @return true合法，false不合法
     */
    @Override
    public Boolean checkFetureExpression(CheckFetureExpressionPara para) {
        PpsOrderEntity orderInfo = ppsOrderService.getPpsOrderInfoByKey(para.getBarcode());
        if (orderInfo == null) {
            throw new InkelinkException("无效的产品条码【" + para.getBarcode() + "】");
        }
        List<String> characteristicsValues = pmProductCharacteristicsVersionsProvider.getCharacteristicsData(orderInfo.getProductCode()
                        , orderInfo.getCharacteristicVersion()).stream().map(PmProductCharacteristicsEntity::getProductCharacteristicsValue)
                .collect(Collectors.toList());
        return FeatureTool.calExpression(para.getFetureExpression(), orderInfo.getModel(), characteristicsValues);
    }

    /**
     * 验证特征表达式
     *
     * @param para 特征表达式
     * @return true合法，false不合法
     */
    @Override
    public Boolean verifyFetureExpression(FetureTestPara para) {
        return FeatureTool.calExpression(para.getPattern(), para.getModel(), new ArrayList<>(Arrays.asList(para.getFeature().split(","))));
    }

}