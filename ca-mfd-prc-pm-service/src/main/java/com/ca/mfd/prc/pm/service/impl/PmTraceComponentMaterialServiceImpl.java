package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmTraceComponentMaterialEntity;
import com.ca.mfd.prc.pm.entity.PmTraceComponentMaterialEntity;
import com.ca.mfd.prc.pm.mapper.IPmTraceComponentMaterialMapper;
import com.ca.mfd.prc.pm.service.IPmTraceComponentMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink ${email}
 * @Description: 追溯组件物料绑定
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmTraceComponentMaterialServiceImpl extends AbstractCrudServiceImpl<IPmTraceComponentMaterialMapper, PmTraceComponentMaterialEntity> implements IPmTraceComponentMaterialService {

    private final String cacheName = "PRC_PM_TRACE_COMPONENT_MATERIAL";
    @Autowired
    private LocalCache localCache;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmTraceComponentMaterialEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmTraceComponentMaterialEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmTraceComponentMaterialEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmTraceComponentMaterialEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PmTraceComponentMaterialEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PmTraceComponentMaterialEntity entity) {
        verfyOperation(entity, true);
    }

    /**
     * 验证方法
     */
    void verfyOperation(PmTraceComponentMaterialEntity model, boolean isUpdate) {

       /* List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("LINE_CODE", model.getLineCode(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PmTraceComponentMaterialEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("线体编码" + model.getLineCode() + "已经存在，不允许重复录入！");
        }*/
    }

    /**
     * 获取所有的数据
     *
     * @return List<PmTraceComponentMaterialEntity>
     */
    @Override
    public List<PmTraceComponentMaterialEntity> getAllDatas() {
        Function<Object, ? extends List<PmTraceComponentMaterialEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }


    @Override
    public PmTraceComponentMaterialEntity getFirstByMaterialNo(String materialNo) {
        QueryWrapper<PmTraceComponentMaterialEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmTraceComponentMaterialEntity::getMaterialNo,materialNo);
        return getTopDatas(1,qry).stream().findFirst().orElse(null);
    }
}