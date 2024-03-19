package com.ca.mfd.prc.pqs.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.IPpsEntryPartsService;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryPartsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class PpsEntryPartsProvider {

    @Autowired
    private IPpsEntryPartsService ppsEntryPartsService;

    /**
     * 获取工单-零部件信息
     *
     * @param entryNo
     * @return
     */
    public PpsEntryPartsEntity getEntryPartsInfoByEntryNo(String entryNo) {
        ResultVO<PpsEntryPartsEntity> result = ppsEntryPartsService.getEntryPartsInfoByEntryNo(entryNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentryparts调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取工单-零部件信息
     *
     * @param planNoOrEntryNo
     * @return
     */
    public PpsEntryPartsEntity getFirstByPlanNoOrEntryNo(String planNoOrEntryNo) {
        ResultVO<PpsEntryPartsEntity> result = ppsEntryPartsService.getFirstByPlanNoOrEntryNo(planNoOrEntryNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentryparts调用失败" + result.getMessage());
        }
        return result.getData();
    }
}