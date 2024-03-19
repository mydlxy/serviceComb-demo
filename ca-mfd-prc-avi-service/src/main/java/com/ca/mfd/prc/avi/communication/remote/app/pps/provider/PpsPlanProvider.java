package com.ca.mfd.prc.avi.communication.remote.app.pps.provider;

import com.ca.mfd.prc.avi.communication.remote.app.pps.IPpsPlanService;
import com.ca.mfd.prc.avi.communication.remote.app.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 线体是否在计划履历中
     *
     * @param lineCode
     * @return
     */
    public Integer hasPlanLine(String lineCode) {
        ResultVO<Integer> result = ppsPlanService.hasPlanLine(lineCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsplan调用失败" + result.getMessage());
        }
        return result.getData();
    }

}