package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.dto.FeatureConfigInfo;
import com.ca.mfd.prc.pm.dto.UpdateFeatureConfigInfo;
import com.ca.mfd.prc.pm.entity.PmCharacteristicsDataEntity;
import com.ca.mfd.prc.pm.entity.PmIssueCharacteristicsConfigEntity;
import com.ca.mfd.prc.pm.mapper.IPmIssueCharacteristicsConfigMapper;
import com.ca.mfd.prc.pm.service.IPmCharacteristicsDataService;
import com.ca.mfd.prc.pm.service.IPmIssueCharacteristicsConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: 下发特征配置服务实现
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Service
public class PmIssueCharacteristicsConfigServiceImpl extends AbstractCrudServiceImpl<IPmIssueCharacteristicsConfigMapper, PmIssueCharacteristicsConfigEntity> implements IPmIssueCharacteristicsConfigService {

    @Autowired
    private IPmCharacteristicsDataService pmCharacteristicsDataService;

    @Override
    public List<PmIssueCharacteristicsConfigEntity> getListBySubKeyRelevanceType(String subKey, Integer relevanceType) {
        QueryWrapper<PmIssueCharacteristicsConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmIssueCharacteristicsConfigEntity::getSubKey, subKey)
                .eq(PmIssueCharacteristicsConfigEntity::getRelevanceType, relevanceType);
        return selectList(qry);
    }

    /**
     * 获取特征项配置
     *
     * @param subKey
     * @param relevanceType
     * @return
     */
    @Override
    public List<FeatureConfigInfo> getFeatureConfigList(String subKey, int relevanceType) {
        List<PmIssueCharacteristicsConfigEntity> dataList = getListBySubKeyRelevanceType(subKey, relevanceType);

        List<FeatureConfigInfo> list = new ArrayList<>();
        list.add(new FeatureConfigInfo("Feature1", StringUtils.EMPTY));
        list.add(new FeatureConfigInfo("Feature2", StringUtils.EMPTY));
        list.add(new FeatureConfigInfo("Feature3", StringUtils.EMPTY));
        list.add(new FeatureConfigInfo("Feature4", StringUtils.EMPTY));

        for (PmIssueCharacteristicsConfigEntity item : dataList) {
            FeatureConfigInfo model = list.stream().filter(c ->
                            StringUtils.equals(c.getFeatureKey(), item.getFeatureKey()))
                    .findFirst().orElse(null);
            if (model != null) {
                model.setFeatureName(item.getFeatureName());
            }
        }
        return list;
    }

    /**
     * 更新特征项配置
     *
     * @param para
     */
    @Override
    public void updateFeatureConfig(UpdateFeatureConfigInfo para) {
        List<PmIssueCharacteristicsConfigEntity> dataList = getListBySubKeyRelevanceType(para.getSubKey(), para.getRelevanceType());
        for (FeatureConfigInfo item : para.getItems()) {
            PmIssueCharacteristicsConfigEntity info = dataList.stream().filter(c ->
                            StringUtils.equals(c.getFeatureKey(), item.getFeatureKey()))
                    .findFirst().orElse(null);
            if (info != null) {
                //赋值为空将删除
                if (StringUtils.isBlank(item.getFeatureName())) {
                    delete(info.getId());
                } else//不为空键修改
                {
                    QueryWrapper<PmCharacteristicsDataEntity> qryCnt = new QueryWrapper<>();
                    qryCnt.lambda().eq(PmCharacteristicsDataEntity::getCharacteristicsName, item.getFeatureName());
                    if (pmCharacteristicsDataService.selectCount(qryCnt) == 0) {
                        throw new InkelinkException(item.getFeatureName() + "在特征数据中未找到对应的特征项");
                    }
                    UpdateWrapper<PmIssueCharacteristicsConfigEntity> upSet = new UpdateWrapper<>();
                    upSet.lambda().set(PmIssueCharacteristicsConfigEntity::getFeatureName, item.getFeatureName())
                            .eq(PmIssueCharacteristicsConfigEntity::getId, info.getId());
                    update(upSet);
                }
            } else {
                if (!StringUtils.isBlank(item.getFeatureName())) {
                    QueryWrapper<PmCharacteristicsDataEntity> qryCnt = new QueryWrapper<>();
                    qryCnt.lambda().eq(PmCharacteristicsDataEntity::getCharacteristicsName, item.getFeatureName());
                    if (pmCharacteristicsDataService.selectCount(qryCnt) == 0) {
                        throw new InkelinkException(item.getFeatureName() + "在特征数据中未找到对应的特征项");
                    }
                    PmIssueCharacteristicsConfigEntity et = new PmIssueCharacteristicsConfigEntity();
                    et.setSubKey(para.getSubKey());
                    et.setRelevanceType(para.getRelevanceType());
                    et.setFeatureKey(item.getFeatureKey());
                    et.setFeatureName(item.getFeatureName());
                    insert(et);
                }
            }
        }
    }
}