package com.ca.mfd.prc.avi.remote.app.pm.provider;

import com.ca.mfd.prc.avi.remote.app.pm.IPmShcCalendarService;
import com.ca.mfd.prc.avi.remote.app.pm.dto.ShcCalendarDetailInfo;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.ShiftDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PmShcCalendarProvider {
    @Autowired
    IPmShcCalendarService pmShcCalendarService;

    public ShiftDTO getCurrentShiftInfo(String lineCode) {
        ResultVO<ShiftDTO> result = pmShcCalendarService.getCurrentShiftInfo(lineCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmshccalendar调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public ShcCalendarDetailInfo getCurrentShift(String lineCode) {
        ResultVO<ShcCalendarDetailInfo> result = pmShcCalendarService.getCurrentShift(lineCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmshccalendar调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
