package com.ca.mfd.prc.pmc.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmTeamLeaderConfigEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.IPmTeamLeaderConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmShcCalendarProvider
 *
 * @author inkelink mason
 * @since 1.0.0 2023-09-12
 */
@Service
public class PmTeamLeaderConfigProvider {
    @Autowired
    private IPmTeamLeaderConfigService pmTeamLeaderConfigService;

    public List<PmTeamLeaderConfigEntity> getData(List<ConditionDto> conditionInfos) {
        ResultVO<List<PmTeamLeaderConfigEntity>> result = pmTeamLeaderConfigService.getData(conditionInfos);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmteamleaderconfig调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
