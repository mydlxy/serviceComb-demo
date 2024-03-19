package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikTcEntity;
import com.ca.mfd.prc.audit.entity.PqsExQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.audit.mapper.IPqsExQualityMatrikMapper;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikAnomalyService;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikService;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikTcService;
import com.ca.mfd.prc.audit.service.IPqsExQualityMatrikWorkstationService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 精致工艺百格图服务实现
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Service
public class PqsExQualityMatrikServiceImpl extends AbstractCrudServiceImpl<IPqsExQualityMatrikMapper, PqsExQualityMatrikEntity> implements IPqsExQualityMatrikService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_EX_QUALITY_MATRIK";
    private void removeCache() {
        localCache.removeObject(cacheName);
    }


    @Autowired
    private IPqsExQualityMatrikTcService pqsExQualityMatrikTcService;
    @Autowired
    private IPqsExQualityMatrikAnomalyService pqsExQualityMatrikAnomalyService;
    @Autowired
    private IPqsExQualityMatrikWorkstationService pqsExQualityMatrikWorkstationService;
    @Resource
    private IPqsExQualityMatrikMapper pqsExQualityMatrikMapper;
    @Override
    public void afterDelete(Wrapper<PqsExQualityMatrikEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsExQualityMatrikEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsExQualityMatrikEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsExQualityMatrikEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void delete(Serializable[] ids) {
        UpdateWrapper<PqsExQualityMatrikWorkstationEntity> delWrap = new UpdateWrapper<>();
        delWrap.lambda().in(PqsExQualityMatrikWorkstationEntity::getPrcPqsExQualityMatrikId, ids);
        pqsExQualityMatrikWorkstationService.delete(delWrap);
        UpdateWrapper<PqsExQualityMatrikAnomalyEntity> anomalyWrap = new UpdateWrapper<>();
        anomalyWrap.lambda().in(PqsExQualityMatrikAnomalyEntity::getPrcPqsExQualityMatrikId, ids);
        pqsExQualityMatrikAnomalyService.delete(anomalyWrap);
        UpdateWrapper<PqsExQualityMatrikTcEntity> tcWrap = new UpdateWrapper<>();
        tcWrap.lambda().in(PqsExQualityMatrikTcEntity::getPrcPqsExQualityMatrikId, ids);
        pqsExQualityMatrikTcService.delete(tcWrap);
        super.delete(ids);
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<PqsExQualityMatrikEntity> getAllDatas() {
        Function<Object, ? extends List<PqsExQualityMatrikEntity>> getDataFunc = (obj) -> {
            List<PqsExQualityMatrikEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsExQualityMatrikEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public PqsExQualityMatrikEntity get(Serializable id) {
        PqsExQualityMatrikEntity data = pqsExQualityMatrikMapper.selectById(id);
        data.setVehicles(pqsExQualityMatrikTcService.getAllDatas().stream()
                .filter(t -> StringUtils.equals(t.getPrcPqsExQualityMatrikId().toString(), id.toString()))
                .collect(Collectors.toList()));
        data.setDefects(pqsExQualityMatrikAnomalyService.getAllDatas().stream()
                .filter(t -> StringUtils.equals(t.getPrcPqsExQualityMatrikId().toString(), id.toString()))
                .collect(Collectors.toList()));
        return data;
    }

}