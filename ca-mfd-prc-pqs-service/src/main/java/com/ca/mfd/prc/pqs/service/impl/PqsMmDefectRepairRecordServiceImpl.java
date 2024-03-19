package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.pqs.dto.MmDefectRepairRecordDto;
import com.ca.mfd.prc.pqs.dto.MmDefectRepairRecordInfo;
import com.ca.mfd.prc.pqs.dto.PpsPlanPartsDto;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectRepairRecordEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsMmDefectRepairRecordMapper;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsPlanPartsProvider;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectRepairRecordService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 缺陷返修记录表服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsMmDefectRepairRecordServiceImpl extends AbstractCrudServiceImpl<IPqsMmDefectRepairRecordMapper, PqsMmDefectRepairRecordEntity> implements IPqsMmDefectRepairRecordService {

    @Autowired
    private PpsPlanPartsProvider ppsPlanPartsProvider;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_MM_DEFECT_REPAIR_RECORD";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsMmDefectRepairRecordEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsMmDefectRepairRecordEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsMmDefectRepairRecordEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsMmDefectRepairRecordEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsMmDefectRepairRecordEntity> getAllDatas() {
        Function<Object, ? extends List<PqsMmDefectRepairRecordEntity>> getDataFunc = (obj) -> {
            List<PqsMmDefectRepairRecordEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsMmDefectRepairRecordEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 根据计划编号，查询生产计划-零部件
     *
     * @param planNo
     * @return
     */
    @Override
    public PpsPlanPartsDto getPpsPlanParts(String planNo) {

        if (StringUtils.isEmpty(planNo)) {
            throw new InkelinkException("计划编号不能为空");
        }
        PpsPlanPartsEntity planPartsEntity = ppsPlanPartsProvider.getPlanPastsByPlanNo(planNo);
        if (planPartsEntity == null) {
            throw new InkelinkException("未找到对应的计划编号信息");
        }
        PpsPlanPartsDto result = new PpsPlanPartsDto();
        BeanUtils.copyProperties(planPartsEntity, result);

        return result;
    }

    /**
     * 保存批次件返修记录
     *
     * @param info
     */
    @Override
    public void savePqsMmDefectRepairRecord(MmDefectRepairRecordInfo info) {

        PqsMmDefectRepairRecordEntity entity = new PqsMmDefectRepairRecordEntity();
        BeanUtils.copyProperties(info, entity);
        // 录入的批次件返修记录为：已处理
        entity.setStatus(1);
        insert(entity);
    }

    /**
     * 批次件返修记录列表
     *
     * @param info
     * @return
     */
    @Override
    public PageData<MmDefectRepairRecordDto> getMmDefectRepairRecordList(DefectAnomalyParaInfo info) {

        List<PqsMmDefectRepairRecordEntity> allDatas = getAllDatas();

        if (StringUtils.isNotEmpty(info.getKey())) {
            allDatas = getAllDatas().stream().filter(a -> a.getRepairNo().contains(info.getKey()))
                    .collect(Collectors.toList());
        }

        PageData<MmDefectRepairRecordDto> pageInfo = new PageData<>();
        List<MmDefectRepairRecordDto> recordDtoList = Lists.newArrayList();
        allDatas.forEach(a -> {
            MmDefectRepairRecordDto recordDto = new MmDefectRepairRecordDto();
            BeanUtils.copyProperties(a, recordDto);
            recordDtoList.add(recordDto);
        });
        recordDtoList.stream().sorted(Comparator.comparing(MmDefectRepairRecordDto::getRepairDt))
                .collect(Collectors.toList());
        pageInfo.setDatas(recordDtoList);
        pageInfo.setTotal(recordDtoList.size());
        pageInfo.setPageIndex(info.getPageIndex());
        pageInfo.setPageSize(info.getPageSize());

        return pageInfo;
    }
}