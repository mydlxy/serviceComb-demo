package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pps.IPpsEntryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PpsEntryReportProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidPpsEntryReportProvider")
public class PpsEntryReportProvider {

    @Autowired
    private IPpsEntryReportService ppsEntryReportService;

    /**
     * 获取
     *
     * @param reportIds
     */
    public void printEntryReport(List<String> reportIds) {
        ResultVO<PmAllDTO> result = ppsEntryReportService.printEntryReport(reportIds);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentryreport调用失败" + result.getMessage());
        }
    }

}