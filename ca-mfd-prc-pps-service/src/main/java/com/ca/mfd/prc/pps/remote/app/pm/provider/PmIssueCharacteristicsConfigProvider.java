package com.ca.mfd.prc.pps.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.IPmIssueCharacteristicsConfigService;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmIssueCharacteristicsConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmIssueCharacteristicsConfigProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmIssueCharacteristicsConfigProvider {

    @Autowired
    private IPmIssueCharacteristicsConfigService pmIssueCharacteristicsConfigService;

    public List<PmIssueCharacteristicsConfigEntity> getListBySubKeyRelevanceType(String subKey, Integer relevanceType) {
        ResultVO<List<PmIssueCharacteristicsConfigEntity>> result = pmIssueCharacteristicsConfigService.getListBySubKeyRelevanceType(subKey, relevanceType);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmissuecharacteristicsconfig调用失败" + result.getMessage());
        }
        return result.getData();
    }
}