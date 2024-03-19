package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.IPpsPlanService;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.eps.remote.app.pps.IPpsOrderService;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PpsOrderProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidPpsPlanProvider")
public class PpsPlanProvider {

    @Autowired
    private IPpsPlanService ppsPlanService;

    /**
     * 获取
     *
     * @param planNo
     * @return
     */
    public PpsPlanEntity getFirstByPlanNo(String planNo) {
        ResultVO<PpsPlanEntity> result = ppsPlanService.getFirstByPlanNo(planNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsplan调用失败" + result.getMessage());
        }
        return result.getData();
    }



}