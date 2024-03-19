package com.ca.mfd.prc.pqs.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.IPpsPlanPartsService;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsPlanPartsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class PpsPlanPartsProvider {

    @Autowired
    private IPpsPlanPartsService ppsPlanPartsService;

    /**
     * 获取生产计划-零部件信息
     *
     * @param planNo
     * @return
     */
    public PpsPlanPartsEntity getPlanPastsByPlanNo(String planNo) {
        ResultVO<PpsPlanPartsEntity> result = ppsPlanPartsService.getPlanPastsByPlanNo(planNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsplanparts调用失败" + result.getMessage());
        }
        return result.getData();
    }

}