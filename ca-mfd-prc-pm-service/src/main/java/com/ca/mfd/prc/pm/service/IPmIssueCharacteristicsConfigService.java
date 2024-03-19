package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.FeatureConfigInfo;
import com.ca.mfd.prc.pm.dto.UpdateFeatureConfigInfo;
import com.ca.mfd.prc.pm.entity.PmIssueCharacteristicsConfigEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 下发特征配置服务
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
public interface IPmIssueCharacteristicsConfigService extends ICrudService<PmIssueCharacteristicsConfigEntity> {

    /**
     * 获取特征项配置
     *
     * @param subKey
     * @param relevanceType
     * @return
     */
    List<PmIssueCharacteristicsConfigEntity> getListBySubKeyRelevanceType(String subKey, Integer relevanceType);

    /**
     * 获取特征项配置
     *
     * @param subKey
     * @param relevanceType
     * @return
     */
    List<FeatureConfigInfo> getFeatureConfigList(String subKey, int relevanceType);

    /**
     * 更新特征项配置
     *
     * @param para
     */
    void updateFeatureConfig(UpdateFeatureConfigInfo para);
}