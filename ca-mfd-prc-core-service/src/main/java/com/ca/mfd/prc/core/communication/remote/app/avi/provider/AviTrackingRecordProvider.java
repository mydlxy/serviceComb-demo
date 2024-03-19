package com.ca.mfd.prc.core.communication.remote.app.avi.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.avi.IAviTrackingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * PmOrgProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class AviTrackingRecordProvider {

    @Autowired
    private IAviTrackingRecordService aviTrackingRecordService;

    public void saveThirdPointData(String sn, String aviCode, Integer aviType, Integer avitrackingenum, Boolean isProcess, Date passTime) {
        ResultVO<String> result = aviTrackingRecordService.saveThirdPointData(sn, aviCode, aviType, avitrackingenum, isProcess, passTime);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-avi-avitrackingrecord调用失败" + result.getMessage());
        }
    }
}