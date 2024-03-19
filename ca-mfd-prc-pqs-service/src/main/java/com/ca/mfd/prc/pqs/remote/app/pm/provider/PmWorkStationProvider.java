package com.ca.mfd.prc.pqs.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pm.IPmWorkStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class PmWorkStationProvider {

    @Autowired
    private IPmWorkStationService pmWorkStationService;

    public ResultVO getCurrentWorkplaceList(int pageIndex, int pageSize, List<ConditionDto> conditions) {
        ResultVO result = pmWorkStationService.getCurrentWorkplaceList(pageIndex, pageSize, conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmworkstation调用失败" + result.getMessage());
        }
        return result;
    }
}
