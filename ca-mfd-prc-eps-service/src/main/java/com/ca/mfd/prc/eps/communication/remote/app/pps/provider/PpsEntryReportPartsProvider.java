package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.IPpsEntryReportPartsService;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryReportPartsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PpsEntryReportProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidPpsEntryReportPartsProvider")
public class PpsEntryReportPartsProvider {

    @Autowired
    private IPpsEntryReportPartsService ppsEntryReportPartsService;

    /**
     * 获取
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