package com.ca.mfd.prc.pqs.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.IPpsEntryReportPartsService;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryReportPartsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class PpsEntryReportPartsProvider {

    @Autowired
    private IPpsEntryReportPartsService ppsEntryReportPartsService;

    /**
     * 获取报工单-零部件信息
     *
     * @param barcode
     * @return
     */
    public PpsEntryReportPartsEntity getFirstByBarcode(String barcode) {
        ResultVO<PpsEntryReportPartsEntity> result = ppsEntryReportPartsService.getFirstByBarcode(barcode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentryreportparts调用失败" + result.getMessage());
        }
        return result.getData();
    }

}