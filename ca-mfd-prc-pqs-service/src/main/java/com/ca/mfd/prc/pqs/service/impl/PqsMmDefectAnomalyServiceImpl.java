package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pqs.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.pqs.entity.PqsDeptEntity;
import com.ca.mfd.prc.pqs.entity.PqsGradeEntity;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsMmDefectAnomalyMapper;
import com.ca.mfd.prc.pqs.service.IPqsDeptService;
import com.ca.mfd.prc.pqs.service.IPqsGradeService;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectAnomalyService;
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
 * @Description: 零部件缺陷记录服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsMmDefectAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsMmDefectAnomalyMapper, PqsMmDefectAnomalyEntity> implements IPqsMmDefectAnomalyService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_MM_DEFECT_ANOMALY";

    @Autowired
    private IPqsGradeService pqsGradeService;
    @Autowired
    private IPqsDeptService pqsDeptService;

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsMmDefectAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsMmDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsMmDefectAnomalyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsMmDefectAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsMmDefectAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsMmDefectAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsMmDefectAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsMmDefectAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取车辆所有缺陷列表
     *
     * @param sn
     * @return
     */
    @Override
    public List<PqsMmDefectAnomalyEntity> getVehicleDefectAnomaly(String sn) {

        return getAllDatas().stream().filter(p -> StringUtils.equals(p.getSn(), sn)).collect(Collectors.toList());
    }

    /**
     * 获取车辆是否有未关闭的缺陷
     *
     * @param sn
     * @return
     */
    @Override
    public Boolean vehicleDefectAnomalyBySn(String sn) {

        boolean result = false;
        List<PqsMmDefectAnomalyEntity> anomalyEntityList = getAllDatas().stream().filter(p -> StringUtils.equals(p.getSn(), sn)
                && p.getStatus() <= 4).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(anomalyEntityList)) {
            result = true;
        }
        return result;
    }

    /**
     * 修改缺陷责任部门-等级
     *
     * @param info
     */
    @Override
    public void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info) {

        PqsGradeEntity gradeInfo = pqsGradeService.getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getGradeCode(), info.getGradeCode())).findFirst().orElse(null);
        PqsDeptEntity deptInfo = pqsDeptService.getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getDeptCode(), info.getResponsibleDeptCode())).findFirst().orElse(null);

        info.getDataIds().forEach(i -> {
            UpdateWrapper<PqsMmDefectAnomalyEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(PqsMmDefectAnomalyEntity::getId, i)
                    .set(PqsMmDefectAnomalyEntity::getResponsibleDeptCode, info.getResponsibleDeptCode())
                    .set(PqsMmDefectAnomalyEntity::getResponsibleDeptName, deptInfo.getDeptName())
                    .set(PqsMmDefectAnomalyEntity::getGradeName, gradeInfo.getGradeName())
                    .set(PqsMmDefectAnomalyEntity::getGradeCode, info.getGradeCode())
                    .set(PqsMmDefectAnomalyEntity::getResponsibleTeamNo, info.getResponsibleTeamNo());
            update(updateWrapper);
        });
    }
}