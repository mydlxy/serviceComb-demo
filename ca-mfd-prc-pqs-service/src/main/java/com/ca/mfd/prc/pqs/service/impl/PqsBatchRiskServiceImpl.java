package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.pqs.dto.EntryAndPlanParasDto;
import com.ca.mfd.prc.pqs.dto.RiskRepairInfo;
import com.ca.mfd.prc.pqs.dto.SubmitBatchRiskInfo;
import com.ca.mfd.prc.pqs.entity.PqsBatchRiskEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsBatchRiskMapper;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsEntryPartsProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsPlanPartsProvider;
import com.ca.mfd.prc.pqs.service.IPqsBatchRiskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 批次件问题排查（质量围堵）服务实现
 * @date 2023年11月08日
 * @变更说明 BY inkelink At 2023年11月08日
 */
@Service
public class PqsBatchRiskServiceImpl extends AbstractCrudServiceImpl<IPqsBatchRiskMapper, PqsBatchRiskEntity> implements IPqsBatchRiskService {

    @Autowired
    private PpsEntryPartsProvider ppsEntryPartsProvider;
    @Autowired
    private PpsPlanPartsProvider ppsPlanPartsProvider;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_BATCH_RISK";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsBatchRiskEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsBatchRiskEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsBatchRiskEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsBatchRiskEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsBatchRiskEntity> getAllDatas() {
        Function<Object, ? extends List<PqsBatchRiskEntity>> getDataFunc = (obj) -> {
            List<PqsBatchRiskEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsBatchRiskEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取计划单号和工单号
     *
     * @param planNoOrEntryNo
     * @return
     */
    @Override
    public EntryAndPlanParasDto getEntryNoAndPlanNo(String planNoOrEntryNo) {

        if (StringUtils.isEmpty(planNoOrEntryNo)) {
            throw new InkelinkException("计划单号或工单号不能为空！");
        }

        // 先找计划单，没有计划单再找工单，没有工单就直接报错
        EntryAndPlanParasDto result;
        PpsPlanPartsEntity planPartsEntity = ppsPlanPartsProvider.getPlanPastsByPlanNo(planNoOrEntryNo);
        if (planPartsEntity == null) {
            PpsEntryPartsEntity entryPartsEntity = ppsEntryPartsProvider.getFirstByPlanNoOrEntryNo(planNoOrEntryNo);
            if (entryPartsEntity == null) {
                throw new InkelinkException("未找到对应的工单号！");
            }
            result = new EntryAndPlanParasDto();
            result.setEntryNo(entryPartsEntity.getEntryNo());
            result.setPlanNo(entryPartsEntity.getPlanNo());
        } else {
            result = new EntryAndPlanParasDto();
            result.setPlanNo(planPartsEntity.getPlanNo());
            PpsEntryPartsEntity entryPartsEntity = ppsEntryPartsProvider.getFirstByPlanNoOrEntryNo(planNoOrEntryNo);
            if (entryPartsEntity != null) {
                result.setEntryNo(entryPartsEntity.getEntryNo());
            }
        }

        return result;
    }

    /**
     * 批次件问题立项
     *
     * @param info
     * @return
     */
    @Override
    public void submitBatchRiskInfo(SubmitBatchRiskInfo info) {

        if (StringUtils.isEmpty(info.getPlanNo()) && StringUtils.isEmpty(info.getEntryNo())) {
            throw new InkelinkException("计划单号、工单号都不存在！");
        }

        PqsBatchRiskEntity entity = new PqsBatchRiskEntity();
        BeanUtils.copyProperties(info, entity);
        entity.setId(IdGenerator.getId());
        entity.setStartBy(identityHelper.getUserName());
        entity.setStartDt(new Date());
        entity.setStatus(1);
        insert(entity);
    }

    /**
     * 关闭问题
     *
     * @param info
     */
    @Override
    public void closeRisk(RiskRepairInfo info) {
        info.getIds().forEach(i -> {
            // 查找问题
            PqsBatchRiskEntity batchRiskEntity = get(i);
            if (batchRiskEntity == null) {
                throw new InkelinkException("未找到对应问题信息");
            }

            // 关闭问题
            batchRiskEntity.setCloseBy(identityHelper.getUserName());
            batchRiskEntity.setCloseRemark(info.getCloseRemark());
            batchRiskEntity.setCloseDt(new Date());
            batchRiskEntity.setStatus(90);
            update(batchRiskEntity);
        });
    }

}