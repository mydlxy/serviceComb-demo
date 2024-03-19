package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.pm.communication.entity.MidCharacteristicsMasterEntity;
import com.ca.mfd.prc.pm.entity.PmCharacteristicsDataEntity;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pm.mapper.IPmCharacteristicsDataMapper;
import com.ca.mfd.prc.pm.service.IPmCharacteristicsDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 特征主数据服务实现
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Service
public class PmCharacteristicsDataServiceImpl extends AbstractCrudServiceImpl<IPmCharacteristicsDataMapper, PmCharacteristicsDataEntity> implements IPmCharacteristicsDataService {

    private static final Object LOCK_OBJ = new Object();
    private final String cacheName = "PRC_PM_CHARACTERISTICS_DATA";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IdentityHelper identityHelper;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmCharacteristicsDataEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmCharacteristicsDataEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmCharacteristicsDataEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmCharacteristicsDataEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PmCharacteristicsDataEntity> getAllDatas() {
        List<PmCharacteristicsDataEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (LOCK_OBJ) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(new ArrayList<>());
                    localCache.addObject(cacheName, datas, -1);
                }
            }
        }
        return datas;
    }


    @Override
    public List<PmCharacteristicsDataEntity> getListByCodes(List<String> codes) {
        QueryWrapper<PmCharacteristicsDataEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PmCharacteristicsDataEntity::getCharacteristicsValue, codes);
        return selectList(qry);
    }

    @Override
    public List<PmCharacteristicsDataEntity> getCharacteristicNames() {
        QueryWrapper<PmCharacteristicsDataEntity> qw = new QueryWrapper<>();
        qw.select("CHARACTERISTICS_NAME","DESCRIPTION_CN");
        qw.groupBy("CHARACTERISTICS_NAME","DESCRIPTION_CN");
        return this.getData(qw,false);
    }

    @Override
    public void syncFromBom(List<MidCharacteristicsMasterEntity> datas) {
        if (CollectionUtils.isEmpty(datas)) {
            throw new InkelinkException("未查询到数据");
        }
        List<PmCharacteristicsDataEntity> adds = new ArrayList<>();
        List<PmCharacteristicsDataEntity> updates = new ArrayList<>();
        datas.stream().forEach(c -> {
            QueryWrapper<PmCharacteristicsDataEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PmCharacteristicsDataEntity::getCharacteristicsValue, c.getFeatureCode())
                    .eq(PmCharacteristicsDataEntity::getCharacteristicsName, c.getFamilyCode());
            PmCharacteristicsDataEntity entity = getTopDatas(1, qry).stream().findFirst().orElse(null);
            if (entity == null) {
                entity = new PmCharacteristicsDataEntity();
                entity.setCharacteristicsName(c.getFamilyCode());
                entity.setDescriptionCn(c.getFamilyName());
                entity.setDescriptionEn(c.getFamilyNameEn());
                entity.setCharacteristicsValue(c.getFeatureCode());
                entity.setValueCn(c.getFeatureName());
                entity.setValueEn(c.getFeatureNameEn());
                entity.setAttribute10("AUTO");
                adds.add(entity);
            } else if (StringUtils.equalsIgnoreCase(entity.getAttribute10(), "AUTO")) {
                entity.setCharacteristicsName(c.getFamilyCode());
                entity.setDescriptionCn(c.getFamilyName());
                entity.setDescriptionEn(c.getFamilyNameEn());
                entity.setCharacteristicsValue(c.getFeatureCode());
                entity.setValueCn(c.getFeatureName());
                entity.setValueEn(c.getFeatureNameEn());
                entity.setAttribute10("AUTO");
                updates.add(entity);
            }
        });
        if (!adds.isEmpty()) {
            this.insertBatch(adds, 200, false, 1);
        }
        if (!updates.isEmpty()) {
            this.updateBatchById(updates, 200, false);
        }
        this.saveChange();
    }
}