package com.ca.mfd.prc.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.audit.entity.PqsAuditEntryDefectAnomalyEntity;
import com.ca.mfd.prc.audit.mapper.IPqsAuditEntryDefectAnomalyMapper;
import com.ca.mfd.prc.audit.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.audit.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.audit.service.IPqsAuditEntryDefectAnomalyService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AUDIT缺陷记录服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsAuditEntryDefectAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsAuditEntryDefectAnomalyMapper, PqsAuditEntryDefectAnomalyEntity> implements IPqsAuditEntryDefectAnomalyService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_AUDIT_ENTRY_DEFECT_ANOMALY";

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsAuditEntryDefectAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsAuditEntryDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsAuditEntryDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsAuditEntryDefectAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsAuditEntryDefectAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsAuditEntryDefectAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsAuditEntryDefectAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsAuditEntryDefectAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public void updateRemark(PqsAuditEntryDefectAnomalyEntity req) {
        if(req.getId()==null){
            throw new InkelinkException("缺陷记录ID不能为空。");
        }
        UpdateWrapper<PqsAuditEntryDefectAnomalyEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsAuditEntryDefectAnomalyEntity::getId, req.getId())
                .set(PqsAuditEntryDefectAnomalyEntity::getRemark, req.getRemark());
        update(updateWrapper);
        saveChange();
    }

    @Override
    public PageData<PqsAuditEntryDefectAnomalyEntity> page(PageDataDto model) {
        return getPageDataEx(model);
    }

    public PageData<PqsAuditEntryDefectAnomalyEntity> getPageDataEx(PageDataDto model) {
        // 获取主表数据
        PageData<PqsAuditEntryDefectAnomalyEntity> pageData = super.page(model);
        // 如果存在主数据
        if (ObjectUtils.isNotEmpty(pageData) && ObjectUtils.isNotEmpty(pageData.getDatas())) {
            List<PqsAuditEntryDefectAnomalyEntity> dataRecord = pageData.getDatas();
            // 获取车辆关联的VIN
            Set<String> vinCodes = new HashSet<>();
            // 获取管理的iccCode列表
            Set<String> iccCodes = new HashSet<>();
            for (PqsAuditEntryDefectAnomalyEntity pqsProductDefectAnomalyEntity : dataRecord) {
                if (StringUtils.isNotBlank(pqsProductDefectAnomalyEntity.getSn())) {
                    vinCodes.add(pqsProductDefectAnomalyEntity.getSn());
                }
                if (StringUtils.isNotBlank(pqsProductDefectAnomalyEntity.getDefectAnomalyCode())) {
                    iccCodes.add(pqsProductDefectAnomalyEntity.getDefectAnomalyCode());
                }
            }

            Map<String, PpsOrderEntity> orderEntityMap = new HashMap<>();
            // 查询关联的车辆数据
            if (ObjectUtils.isNotEmpty(vinCodes)) {
                List<PpsOrderEntity> ppsOrderEntityList = ppsOrderProvider.getListBySnCodes(new ArrayList<>(vinCodes));
                orderEntityMap = ppsOrderEntityList.stream().collect(Collectors.toMap(PpsOrderEntity::getSn, item -> item));
            }


            // 赋值需要关联的信息
            for (PqsAuditEntryDefectAnomalyEntity item : dataRecord) {
                if (orderEntityMap.containsKey(item.getSn())) {
                    PpsOrderEntity orderEntity = orderEntityMap.get(item.getSn());
                    if (null != orderEntity) {
                        item.setModel(orderEntity.getModel());
                    }
                }
            }
        }
        return pageData;
    }
    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {

        datas.forEach(data -> {
            // 导出处理时间格式化
            if (data.containsKey("activateTime") && data.getOrDefault("activateTime", null) != null) {
                data.put("activateTime", DateUtils.format((Date) data.get("activateTime"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("repairTime") && data.getOrDefault("repairTime", null) != null) {
                data.put("repairTime", DateUtils.format((Date) data.get("repairTime"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("recheckTime") && data.getOrDefault("recheckTime", null) != null) {
                data.put("recheckTime", DateUtils.format((Date) data.get("recheckTime"), DateUtils.DATE_TIME_PATTERN));
            }

            // 状态处理
            if (data.containsKey("status") && data.getOrDefault("status", null) != null) {
                switch (String.valueOf(data.get("status"))) {
                    case "1":
                        data.put("status", "已激活");
                        break;
                    case "2":
                        data.put("status", "已修复");
                        break;
                    case "3":
                        data.put("status", "未发现");
                        break;
                    case "4":
                        data.put("status", "合格");
                        break;
                    case "5":
                        data.put("status", "不合格");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}