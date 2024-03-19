package com.ca.mfd.prc.eps.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.IPmShcCalendarService;
import com.ca.mfd.prc.eps.remote.app.pm.dto.ShiftDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PmShcCalendarProvider
 *
 * @author inkelink mason
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmShcCalendarProvider {

    @Autowired
    private IPmShcCalendarService pmShcCalendarService;

    public ShiftDTO getCurrentShiftInfo(String lineCode) {
        ResultVO<ShiftDTO> result = pmShcCalendarService.getCurrentShiftInfo(lineCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmshccalendar调用失败" + result.getMessage());
        }
        return result.getData();
    }

}