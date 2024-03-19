package com.ca.mfd.prc.eps.remote.app.pqs.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pqs.IPqsLogicService;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.ActiveAnomalyInfo;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.AnomalyActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PmShcCalendarProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PqsLogicProvider {

    @Autowired
    private IPqsLogicService pqsLogicService;

    /**
     * 获取
     *
     * @param anomalyActivity
     * @return
     */
    public String modifyDefectAnomalyStatus(AnomalyActivity anomalyActivity) {
        ResultVO<String> result = pqsLogicService.modifyDefectAnomalyStatus(anomalyActivity);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pqs-pqslogic调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param activeAnomalyInfo
     * @return
     */
    public String activeAnomaly(ActiveAnomalyInfo activeAnomalyInfo) {
        ResultVO<String> result = pqsLogicService.activeAnomaly(activeAnomalyInfo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pqs-pqslogic调用失败" + result.getMessage());
        }
        return result.getData();
    }

}