package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsPackStartWorkConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsPackStartWorkConfigMapper;
import com.ca.mfd.prc.pps.entity.PpsPackStartWorkConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsPackStartWorkConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @Description: 电池开工配置服务实现
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
@Service
public class PpsPackStartWorkConfigServiceImpl extends AbstractCrudServiceImpl<IPpsPackStartWorkConfigMapper, PpsPackStartWorkConfigEntity> implements IPpsPackStartWorkConfigService {

    private final String cacheName = "PRC_PPS_PACK_START_WORK_CONFIG";
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PpsPackStartWorkConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsPackStartWorkConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PpsPackStartWorkConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsPackStartWorkConfigEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PpsPackStartWorkConfigEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PpsPackStartWorkConfigEntity entity) {
        verfyOperation(entity, true);
    }

    /**
     * 验证方法
     */
    void verfyOperation(PpsPackStartWorkConfigEntity model, boolean isUpdate) {

        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("LINE_CODE", model.getLineCode(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PpsPackStartWorkConfigEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("线体编码" + model.getLineCode() + "已经存在，不允许重复录入！");
        }
    }

    /**
     * 获取所有的数据
     *
     * @return List<PpsPackStartWorkConfigEntity>
     */
    @Override
    public List<PpsPackStartWorkConfigEntity> getAllDatas() {
        Function<Object, ? extends List<PpsPackStartWorkConfigEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }
}