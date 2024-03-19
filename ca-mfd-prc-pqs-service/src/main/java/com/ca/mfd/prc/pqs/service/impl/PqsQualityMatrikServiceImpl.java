package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikTcEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikWorkstationEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsQualityMatrikMapper;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikService;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikTcService;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikWorkstationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 百格图服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsQualityMatrikServiceImpl extends AbstractCrudServiceImpl<IPqsQualityMatrikMapper, PqsQualityMatrikEntity> implements IPqsQualityMatrikService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_QUALITY_MATRIK";
    @Autowired
    private IPqsQualityMatrikTcService pqsQualityMatrikTcService;
    @Autowired
    private IPqsQualityMatrikAnomalyService pqsQualityMatrikAnomalyService;
    @Autowired
    private IPqsQualityMatrikWorkstationService pqsQualityMatrikWorkstationService;
    @Autowired
    private IPqsQualityMatrikMapper pqsQualityMatrikMapper;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsQualityMatrikEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsQualityMatrikEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsQualityMatrikEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsQualityMatrikEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void delete(Serializable[] ids) {
        UpdateWrapper<PqsQualityMatrikWorkstationEntity> delWrap = new UpdateWrapper<>();
        delWrap.lambda().in(PqsQualityMatrikWorkstationEntity::getPrcPqsQualityMatrikId, ids);
        pqsQualityMatrikWorkstationService.delete(delWrap);
        UpdateWrapper<PqsQualityMatrikAnomalyEntity> anomalyWrap = new UpdateWrapper<>();
        anomalyWrap.lambda().in(PqsQualityMatrikAnomalyEntity::getPrcPqsQualityMatrikId, ids);
        pqsQualityMatrikAnomalyService.delete(anomalyWrap);
        UpdateWrapper<PqsQualityMatrikTcEntity> tcWrap = new UpdateWrapper<>();
        tcWrap.lambda().in(PqsQualityMatrikTcEntity::getPrcPqsQualityMatrikId, ids);
        pqsQualityMatrikTcService.delete(tcWrap);
        super.delete(ids);
    }

    /**
     * 获取所有数据
     */
    @Override
    public List<PqsQualityMatrikEntity> getAllDatas() {
        Function<Object, ? extends List<PqsQualityMatrikEntity>> getDataFunc = (obj) -> {
            List<PqsQualityMatrikEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsQualityMatrikEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    private List<PqsQualityMatrikEntity> getDatas() {
        QueryWrapper<PqsQualityMatrikEntity> qry = new QueryWrapper<>();
        qry.lambda().orderByAsc(PqsQualityMatrikEntity::getModels).orderByAsc(PqsQualityMatrikEntity::getDisplayNo);
        List<PqsQualityMatrikEntity> datas = selectList(qry);
        for (PqsQualityMatrikEntity item : datas) {
            item.setVehicles(pqsQualityMatrikTcService.getAllDatas().stream()
                    .filter(t -> StringUtils.equals(t.getPrcPqsQualityMatrikId().toString(), item.getId().toString()))
                    .collect(Collectors.toList()));
            item.setDefects(pqsQualityMatrikAnomalyService.getAllDatas().stream()
                    .filter(t -> StringUtils.equals(t.getPrcPqsQualityMatrikId().toString(), item.getId().toString()))
                    .collect(Collectors.toList()));
        }
        return datas;
    }

    @Override
    public PqsQualityMatrikEntity get(Serializable id) {
        PqsQualityMatrikEntity data = pqsQualityMatrikMapper.selectById(id);
        data.setVehicles(pqsQualityMatrikTcService.getAllDatas().stream()
                .filter(t -> StringUtils.equals(t.getPrcPqsQualityMatrikId().toString(), id.toString()))
                .collect(Collectors.toList()));
        data.setDefects(pqsQualityMatrikAnomalyService.getAllDatas().stream()
                .filter(t -> StringUtils.equals(t.getPrcPqsQualityMatrikId().toString(), id.toString()))
                .collect(Collectors.toList()));
        return data;
    }
}