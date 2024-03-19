package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.pqs.dto.PqsEntryCheckItemDto;
import com.ca.mfd.prc.pqs.entity.PqsEntryCheckItemEntity;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryCheckItemMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryCheckItemService;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateItemService;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 工单检验项服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryCheckItemServiceImpl extends AbstractCrudServiceImpl<IPqsEntryCheckItemMapper, PqsEntryCheckItemEntity> implements IPqsEntryCheckItemService {
    @Autowired
    private IPqsInspectionTemplateService pqsInspectionTemplateService;

    @Autowired
    private IPqsInspectionTemplateItemService pqsInspectionTemplateItemService;

    @Resource
    private IPqsEntryCheckItemMapper pqsEntryCheckItemDao;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_CHECK_ITEM";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryCheckItemEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryCheckItemEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryCheckItemEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryCheckItemEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的工单检验项信息
     *
     * @return
     */
    @Override
    public List<PqsEntryCheckItemEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryCheckItemEntity>> getDataFunc = (obj) -> {
            List<PqsEntryCheckItemEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryCheckItemEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 重置检验项目 （删除所有检验项）
     *
     * @param inspectionNo 质检单号
     * @return 空检验项列表
     */
    @Override
    public List<PqsEntryCheckItemDto> restCheckItem(String inspectionNo) {

        if (StringUtils.isEmpty(inspectionNo)) {
            throw new InkelinkException("质检单号不能为空");
        }

        List<ConditionDto> conditionDtos = new ArrayList<>(1);
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("inspectionNo");
        conditionDto.setValue(inspectionNo);
        conditionDto.setOperator(ConditionOper.Equal);
        conditionDtos.add(conditionDto);
        delete(conditionDtos);
        return new ArrayList<PqsEntryCheckItemDto>();
    }

    /**
     * 删除检验项
     *
     * @param id 检验项ID
     */
    @Override
    public void deleteCheckItem(String id) {
        List<ConditionDto> conditionDtos = new ArrayList<>(1);
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("id");
        conditionDto.setValue(id);
        conditionDto.setOperator(ConditionOper.Equal);
        conditionDtos.add(conditionDto);
        delete(conditionDtos);
    }

    /**
     * 初始化检验项目
     *
     * @param inspectionNo 检验工单号
     * @param templateId   模板ID
     * @return 是否需要复检，检验项列表
     */
    @Override
    public List<PqsEntryCheckItemDto> initCheckItem(String inspectionNo, Long templateId) {
        return doInsertCheckItem(inspectionNo, templateId);
    }

    /**
     * 执行初始化检验项操作
     *
     * @param inspectionNo 检验工单号
     * @param templateId   模板ID
     * @return 是否需要复检，检验项列表
     */
    private List<PqsEntryCheckItemDto> doInsertCheckItem(String inspectionNo, Long templateId) {

        PqsInspectionTemplateEntity template = pqsInspectionTemplateService.get(templateId);
        if (template == null) {
            return new ArrayList<PqsEntryCheckItemDto>();
        }

        List<PqsEntryCheckItemEntity> checkItem = pqsEntryCheckItemDao.getNewPqsEntryCheckItem(templateId);
        checkItem.forEach(c -> {
            c.setId(IdGenerator.getId());
            c.setInspectionNo(inspectionNo);
            c.setCheckResult(0);
            c.setCheckValue(StringUtils.EMPTY);
        });
        insertBatch(checkItem, checkItem.size());

        List<PqsEntryCheckItemDto> resultList = Lists.newArrayList();
        checkItem.forEach(w -> {
            PqsEntryCheckItemDto dto = new PqsEntryCheckItemDto();
            BeanUtils.copyProperties(w, dto);
            resultList.add(dto);
        });

        return resultList.stream().sorted(Comparator.comparing(PqsEntryCheckItemDto::getGroupName)).collect(Collectors.toList());
    }

    /**
     * 获取检验项列表
     *
     * @param inspectionNo 检验工单号
     * @return 检验项列表
     */
    @Override
    public List<PqsEntryCheckItemDto> getCheckItem(String inspectionNo) {
        List<ConditionDto> conditionDtos = new ArrayList<>(1);
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("inspectionNo");
        conditionDto.setValue(inspectionNo);
        conditionDto.setOperator(ConditionOper.Equal);
        conditionDtos.add(conditionDto);
        return getData(conditionDtos).stream().map(w -> {
            PqsEntryCheckItemDto dto = new PqsEntryCheckItemDto();
            BeanUtils.copyProperties(w, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 保存检验结果
     *
     * @param checkItem
     * @return 检验项列表
     */
    @Override
    public List<PqsEntryCheckItemDto> saveCheckItem(List<PqsEntryCheckItemDto> checkItem) {

        // 修改
        List<PqsEntryCheckItemDto> updLists = checkItem.stream()
                .filter(w -> !w.getId().equals(Constant.DEFAULT_ID)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(updLists)) {
            for (PqsEntryCheckItemDto entity : updLists) {
                LambdaUpdateWrapper<PqsEntryCheckItemEntity> updateWrapper = new LambdaUpdateWrapper();
                updateWrapper.eq(PqsEntryCheckItemEntity::getId, entity.getId())
                        .set(PqsEntryCheckItemEntity::getCheckValue, entity.getCheckValue())
                        .set(PqsEntryCheckItemEntity::getRemark, entity.getRemark())
                        .set(PqsEntryCheckItemEntity::getCheckResult, entity.getCheckResult());
                update(updateWrapper);
            }
        }

        // 新增
        List<PqsEntryCheckItemDto> newLists = checkItem.stream()
                .filter(w -> w.getId().equals(Constant.DEFAULT_ID)).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(newLists)) {
            List<PqsEntryCheckItemEntity> saveLists = Lists.newArrayList();
            for (PqsEntryCheckItemDto entity : newLists) {
                PqsEntryCheckItemEntity info = new PqsEntryCheckItemEntity();
                BeanUtils.copyProperties(entity, info);
                info.setId(IdGenerator.getId());
                saveLists.add(info);
            }
            insertBatch(saveLists, saveLists.size());
        }
        return checkItem;
    }
}