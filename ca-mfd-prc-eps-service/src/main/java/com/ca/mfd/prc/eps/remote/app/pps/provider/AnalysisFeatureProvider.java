package com.ca.mfd.prc.eps.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.IAnalysisFeatureService;
import com.ca.mfd.prc.eps.remote.app.pps.dto.FilterFetureExpressionPara;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmShcCalendarProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class AnalysisFeatureProvider {

    @Autowired
    private IAnalysisFeatureService analysisFeatureService;

    /**
     * 获取
     *
     * @param para
     * @return
     */
    public List<String> filterFeatureExpression(FilterFetureExpressionPara para) {
        ResultVO<List<String>> result = analysisFeatureService.filterFeatureExpression(para);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-analysisfeture调用失败" + result.getMessage());
        }
        return result.getData();
    }

}