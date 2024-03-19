package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsPackBranchingPlanConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsPackBranchingPlanConfigMapper;
import com.ca.mfd.prc.pps.entity.PpsPackBranchingPlanConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsPackBranchingPlanConfigService;
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
 * @Description: 电池分线计划配置服务实现
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
@Service
public class PpsPackBranchingPlanConfigServiceImpl extends AbstractCrudServiceImpl<IPpsPackBranchingPlanConfigMapper, PpsPackBranchingPlanConfigEntity> implements IPpsPackBranchingPlanConfigService {

    private final String cacheName = "PRC_PPS_PACK_BRANCHING_PLAN_CONFIG";
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PpsPackBranchingPlanConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsPackBranchingPlanConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PpsPackBranchingPlanConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsPackBranchingPlanConfigEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PpsPackBranchingPlanConfigEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PpsPackBranchingPlanConfigEntity entity) {
        verfyOperation(entity, true);
    }

    /**
     * 验证方法
     */
    void verfyOperation(PpsPackBranchingPlanConfigEntity model, boolean isUpdate) {

        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("LINE_CODE", model.getLineCode(), ConditionOper.Equal));
        //conditionInfos.add(new ConditionDto("COMPONENT_CODE", model.getComponentCode(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PpsPackBranchingPlanConfigEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("线体" + model.getLineCode() + "已经存在，不允许重复录入！");
            // + "和组件" + model.getComponentCode()
        }
    }

    /**
     * 获取所有的数据
     *
     * @return List<PpsPackBranchingPlanConfigEntity>
     */
    @Override
    public List<PpsPackBranchingPlanConfigEntity> getAllDatas() {
        Function<Object, ? extends List<PpsPackBranchingPlanConfigEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }
}