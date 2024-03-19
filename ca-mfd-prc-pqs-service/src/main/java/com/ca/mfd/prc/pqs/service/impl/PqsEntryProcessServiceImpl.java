package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.pqs.dto.CreateProcessEntryInfo;
import com.ca.mfd.prc.pqs.dto.EntryProcessDto;
import com.ca.mfd.prc.pqs.dto.GetPartsProcessInfo;
import com.ca.mfd.prc.pqs.dto.PqsEntryPageFilter;
import com.ca.mfd.prc.pqs.dto.PqsEntryProcessDto;
import com.ca.mfd.prc.pqs.dto.SaveEntryProcessInfo;
import com.ca.mfd.prc.pqs.entity.PqsEntryCheckItemEntity;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryProcessMapper;
import com.ca.mfd.prc.pqs.remote.app.core.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pqs.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysQueueNoteProvider;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsEntryPartsProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsEntryReportPartsProvider;
import com.ca.mfd.prc.pqs.service.IPqsEntryCheckItemService;
import com.ca.mfd.prc.pqs.service.IPqsEntryProcessService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 过程检验服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryProcessServiceImpl extends AbstractCrudServiceImpl<IPqsEntryProcessMapper, PqsEntryProcessEntity> implements IPqsEntryProcessService {

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private PpsEntryPartsProvider ppsEntryPartsProvider;
    @Autowired
    private PpsEntryReportPartsProvider ppsEntryReportPartsProvider;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private IPqsEntryCheckItemService pqsEntryCheckItemService;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private SysQueueNoteProvider sysQueueNoteProvider;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_PROCESS";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryProcessEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryProcessEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryProcessEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryProcessEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的过程检验信息
     *
     * @return
     */
    @Override
    public List<PqsEntryProcessEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryProcessEntity>> getDataFunc = (obj) -> {
            List<PqsEntryProcessEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryProcessEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 根据工单号或条码获取零件信息
     *
     * @param key
     * @return
     */
    @Override
    public GetPartsProcessInfo getPartProcessInfo(String key) {

        GetPartsProcessInfo partsInfo = null;
        // 先通过单件条码查找
        PpsEntryReportPartsEntity rpt = ppsEntryReportPartsProvider.getFirstByBarcode(key);
        if (rpt != null) {
            PpsEntryPartsEntity entry = ppsEntryPartsProvider.getEntryPartsInfoByEntryNo(rpt.getEntryNo());
            if (entry != null) {
                partsInfo = new GetPartsProcessInfo();
                partsInfo.setBarcode(rpt.getBarcode());
                partsInfo.setModel(entry.getModel());
                partsInfo.setProcessCode(rpt.getProcessCode());
                partsInfo.setProcessName(rpt.getProcessName());
                partsInfo.setEntryNo(entry.getEntryNo());
                partsInfo.setMaterialNo(entry.getMaterialNo());
                partsInfo.setMaterialCn(entry.getMaterialCn());
                partsInfo.setOrderCategory(entry.getOrderCategory());
                partsInfo.setEntryReportNo(rpt.getEntryReportNo());
            }
        }
        if (partsInfo == null) {
            PpsEntryPartsEntity entry = ppsEntryPartsProvider.getEntryPartsInfoByEntryNo(key);
            if (entry != null) {
                partsInfo = new GetPartsProcessInfo();
                BeanUtils.copyProperties(entry, partsInfo);
                partsInfo.setBarcode(StringUtils.EMPTY);
                partsInfo.setEntryReportNo(StringUtils.EMPTY);
            }
        }

        return partsInfo;
    }

    /**
     * 获取工单列表
     *
     * @param filter
     * @return
     */
    @Override
    public PageData<PqsEntryProcessDto> getEntryList(PqsEntryPageFilter filter) {

        int[] statusArray = filter.getStaus();
        QueryWrapper<PqsEntryProcessEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsEntryProcessEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsEntryProcessEntity::getEntryType, filter.getEntryType())
                .in(PqsEntryProcessEntity::getStatus, Arrays.asList(Arrays.stream(statusArray).boxed().toArray(Integer[]::new)));
        if (StringUtils.isNotBlank(filter.getEntryType())) {
            lambdaQueryWrapper.eq(PqsEntryProcessEntity::getEntryType, filter.getEntryType());
        }
        if (StringUtils.isNotBlank(filter.getProcessCode())) {
            lambdaQueryWrapper.eq(PqsEntryProcessEntity::getProcessCode, filter.getProcessCode());
        }
        if (StringUtils.isNotBlank(filter.getKey())) {
            lambdaQueryWrapper.like(PqsEntryProcessEntity::getInspectionNo, filter.getKey());
            lambdaQueryWrapper.or();
            lambdaQueryWrapper.like(PqsEntryProcessEntity::getBarcode, filter.getKey());
            lambdaQueryWrapper.or();
            lambdaQueryWrapper.like(PqsEntryProcessEntity::getMaterialNo, filter.getKey());
            lambdaQueryWrapper.or();
            lambdaQueryWrapper.like(PqsEntryProcessEntity::getMaterialCn, filter.getKey());
        }
        lambdaQueryWrapper.orderByAsc(PqsEntryProcessEntity::getStatus)
                .orderByDesc(PqsEntryProcessEntity::getQcDt)
                .orderByDesc(PqsEntryProcessEntity::getInspectionNo);

        List<PqsEntryProcessDto> dataLists = Lists.newArrayList();
        Page<PqsEntryProcessEntity> page = (Page<PqsEntryProcessEntity>) getDataByPage(queryWrapper, filter.getPageIndex(), filter.getPageSize());
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(w -> {
                PqsEntryProcessDto dto = new PqsEntryProcessDto();
                BeanUtils.copyProperties(w, dto);
                dataLists.add(dto);
            });
        }

        PageData<PqsEntryProcessDto> result = new PageData<>();
        result.setDatas(dataLists);
        result.setTotal(dataLists.size());
        result.setPageIndex(filter.getPageIndex());
        result.setPageSize(filter.getPageSize());

        return result;
    }

    /**
     * 初始化工单
     *
     * @param inspectionNo
     * @return
     */
    @Override
    public PqsEntryProcessDto initialization(String inspectionNo) {

        PqsEntryProcessEntity inspectionInfo = getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getInspectionNo(), inspectionNo)).findFirst().orElse(null);
        if (inspectionInfo == null) {
            throw new InkelinkException("未找到对应检验单！");
        }

        // 更新状态
        if (inspectionInfo.getStatus() == 1) {
            UpdateWrapper<PqsEntryProcessEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(PqsEntryProcessEntity::getInspectionNo, inspectionInfo.getInspectionNo())
                    .eq(PqsEntryProcessEntity::getStatus, 1)
                    .set(PqsEntryProcessEntity::getStatus, 2);
            update(updateWrapper);
        }

        PqsEntryProcessDto result = null;
        PqsEntryProcessEntity entryProcessEntity = getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getInspectionNo(), inspectionNo)).findFirst().orElse(null);
        if (entryProcessEntity != null) {
            result = new PqsEntryProcessDto();
            BeanUtils.copyProperties(entryProcessEntity, result);
        }

        return result;
    }

    /**
     * 创建工单
     *
     * @param info
     * @return
     */
    @Override
    public String createProcessEntry(CreateProcessEntryInfo info) {

        // 生产工单
        PpsEntryPartsEntity entry = null;
        if (StringUtils.isNotEmpty(info.getEntryNo())) {
            entry = ppsEntryPartsProvider.getEntryPartsInfoByEntryNo(info.getEntryNo());
        }
        if (entry == null && StringUtils.isNotEmpty(info.getBarcode())) {
            // 根据报工单取最后一个工单
            PpsEntryReportPartsEntity rpt = ppsEntryReportPartsProvider.getFirstByBarcode(info.getBarcode());
            if (rpt != null) {
                PpsEntryPartsEntity ppsentry = ppsEntryPartsProvider.getEntryPartsInfoByEntryNo(rpt.getEntryNo());
                if (ppsentry != null) {
                    entry = new PpsEntryPartsEntity();
                    BeanUtils.copyProperties(ppsentry, entry);
                    entry.setCreationDate(rpt.getCreationDate());
                }
            }
        }
        if (entry == null) {
            throw new InkelinkException("未找到对应生产工单！");
        }

        SysConfigurationEntity entryTypes = sysConfigurationProvider.getAllDatas().stream()
                .filter(s -> StringUtils.equals(s.getCategory(), "PqsEntryType")
                        && StringUtils.equals(s.getValue(), info.getEntryType())).findFirst().orElse(null);
        // 拼接SN
        String snCode = entry.getWorkshopCode() + "_" + info.getEntryType();
        Map<String, String> para = new HashMap<>();
        para.put("processCode", entry.getProcessCode());
        String inspectionNo = sysSnConfigProvider.createSnBypara(snCode, para);

        PqsEntryProcessEntity inspectionEntry = new PqsEntryProcessEntity();
        BeanUtils.copyProperties(entry, inspectionEntry);
        inspectionEntry.setId(IdGenerator.getId());
        inspectionEntry.setInspectionNo(inspectionNo);
        inspectionEntry.setEntryReportNo(info.getEntryReportNo());
        inspectionEntry.setBarcode(info.getBarcode());
        inspectionEntry.setEntryType(info.getEntryType());
        inspectionEntry.setEntryTypeDesc(entryTypes == null ? StringUtils.EMPTY : entryTypes.getText());
        inspectionEntry.setStatus(1);
        inspectionEntry.setSampleQty(1);
        insert(inspectionEntry);

        return inspectionEntry.getInspectionNo();
    }

    /**
     * 保存工单结果
     *
     * @param info
     */
    @Override
    public void saveEntryProcessResult(SaveEntryProcessInfo info) {
        // 评审工单
        PqsEntryProcessEntity entryProcess = getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getInspectionNo(), info.getInspectionNo()))
                .findFirst().orElse(null);
        // 过程
        if (entryProcess == null) {
            throw new InkelinkException("未找到检验工单！");
        }
        // 工单
        if (entryProcess.getStatus() >= 90) {
            throw new InkelinkException("工单已完成，请勿重复提交！");
        }
        // 查找未完成检查项
        List<PqsEntryCheckItemEntity> checkItemLists = pqsEntryCheckItemService.getAllDatas().stream()
                .filter(p -> StringUtils.equals(p.getInspectionNo(), info.getInspectionNo())
                        && p.getCheckResult() == 0).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(checkItemLists)) {
            throw new InkelinkException("请先填写完成检验项！");
        }
        // 修改结果
        UpdateWrapper<PqsEntryProcessEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsEntryProcessEntity::getId, entryProcess.getId())
                .set(PqsEntryProcessEntity::getStatus, 90)
                .set(PqsEntryProcessEntity::getQcUser, identityHelper.getUserName())
                .set(PqsEntryProcessEntity::getQcDt, new Date())
                .set(PqsEntryProcessEntity::getRemark, info.getRemark())
                .set(PqsEntryProcessEntity::getResult, info.getReuslt());
        update(updateWrapper);

        EntryProcessDto dto = new EntryProcessDto();
        dto.setEntryReportNo(entryProcess.getEntryReportNo());
        dto.setPqsEntryType(entryProcess.getEntryType());
        dto.setInspectionNo(info.getInspectionNo());
        dto.setIsQualified(info.getReuslt() == 1);
        // 条码不能为空，推送条码结果
        if (StringUtils.isNotEmpty(entryProcess.getBarcode())) {
            SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
            sysQueueNoteEntity.setGroupName("PqsEntryResultNotify");
            sysQueueNoteEntity.setContent(JSONUtil.toJsonStr(dto));
            sysQueueNoteProvider.addSimpleMessage(sysQueueNoteEntity);
        }
    }

    /**
     * 删除工单
     *
     * @param inspectionNo
     */
    @Override
    public void deleteEntry(String inspectionNo) {

        UpdateWrapper<PqsEntryProcessEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsEntryProcessEntity::getEntryNo, inspectionNo);
        delete(updateWrapper);
    }
}