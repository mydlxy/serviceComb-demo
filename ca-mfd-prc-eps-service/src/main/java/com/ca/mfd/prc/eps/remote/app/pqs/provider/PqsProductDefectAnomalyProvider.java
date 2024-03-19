package com.ca.mfd.prc.eps.remote.app.pqs.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pqs.IPqsProductDefectAnomalyService;
import com.ca.mfd.prc.eps.remote.app.pqs.entity.PqsProductDefectAnomalyEntity;
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
public class PqsProductDefectAnomalyProvider {

    @Autowired
    private IPqsProductDefectAnomalyService pqsProductDefectAnomalyService;

    /**
     * 获取
     *
     * @param conditions
     * @return
     */
    public List<PqsProductDefectAnomalyEntity> getData(List<ConditionDto> conditions) {
        ResultVO<List<PqsProductDefectAnomalyEntity>> result = pqsProductDefectAnomalyService.getData(conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pqs-pqsproductdefectanomaly调用失败" + result.getMessage());
        }
        return result.getData();
    }

}