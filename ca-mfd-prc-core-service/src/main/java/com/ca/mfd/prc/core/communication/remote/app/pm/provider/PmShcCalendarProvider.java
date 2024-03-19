package com.ca.mfd.prc.core.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.IPmShcCalendarService;
import com.ca.mfd.prc.core.communication.remote.app.pm.dto.ShcWorkTimeInfo;
import com.ca.mfd.prc.core.communication.remote.app.pm.dto.ShcWorkTimePara;
import com.ca.mfd.prc.core.communication.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmShcCalendarEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmShcCalendarProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmShcCalendarProvider {

    @Autowired
    private IPmShcCalendarService pmShcCalendarService;

    public List<PmShcCalendarEntity> getData(List<ConditionDto> conditions) {
        ResultVO<List<PmShcCalendarEntity>> result = pmShcCalendarService.getData(conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmshccalendar调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public ShiftDTO getCurrentShiftInfo(String lineCode) {
        ResultVO<ShiftDTO> result = pmShcCalendarService.getCurrentShiftInfo(lineCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmshccalendar调用失败" + result.getMessage());
        }
        return result.getData();
    }


    public List<ShcWorkTimeInfo> getWorkTimes(ShcWorkTimePara para) {
        ResultVO<List<ShcWorkTimeInfo>> result = pmShcCalendarService.getWorkTimes(para);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmshccalendar调用失败" + result.getMessage());
        }
        return result.getData();
    }
}