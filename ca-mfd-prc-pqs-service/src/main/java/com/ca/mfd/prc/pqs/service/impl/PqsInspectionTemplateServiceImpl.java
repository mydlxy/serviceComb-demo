package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.*;
import com.ca.mfd.prc.pqs.dto.TemplateCopyDto;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateEntity;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateItemEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsInspectionTemplateMapper;
import com.ca.mfd.prc.pqs.remote.app.pm.IPmProductMaterialMasterService;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateItemService;
import com.ca.mfd.prc.pqs.service.IPqsInspectionTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 检验模板管理服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsInspectionTemplateServiceImpl extends AbstractCrudServiceImpl<IPqsInspectionTemplateMapper, PqsInspectionTemplateEntity> implements IPqsInspectionTemplateService {

    private final Map<String, Map<String, String>> orderDic = new HashMap<>();

    private final Map<String, Map<String, String>> orderDicMa = new HashMap<>();
    @Autowired
    IPqsInspectionTemplateItemService pqsInspectionTemplateItemService;
    @Resource
    IPqsInspectionTemplateMapper pqsInspectionTemplateMapper;
    @Autowired
    IdentityHelper identityHelper;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_INSPECTION_TEMPLATE";
    @Autowired
    private IPmProductMaterialMasterService pmProductMaterialMasterService;

    {
        Map<String, String> mpStamping = new LinkedHashMap<>();
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getDisplayNo), "显示顺序");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getMaterialNo), "零件号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getMaterialCn), "零件描述");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTemplateCode), "模板代码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTemplateName), "模板名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getProcessCode), "检测工位");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getProcessName), "检测工位名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getIsEnabled), "是否启用");
        // mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getIsRecheck), "是否需要复检");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getRemark), "模板备注");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getDisplayNo1), "检验项显示顺序");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemTypeName), "检验项类型名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getStandard), "检验标准");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemCode), "检测项编码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemName), "检测项名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute6), "级别码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute7  ), "特性码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute8), "特征码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemTypeCode), "检验项类型");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute1), "特征号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute2), "首末孔");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute3), "轴号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute4), "刀具号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute5), "量具");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute9), "特征类型");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTarget), "特征尺寸");
        // mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTarget), "目标值");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getLowerLimit), "下限值");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getUpperLimit), "上限值");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getUnit), "单位");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getRemark1), "备注");
        orderDic.put("检验模板", mpStamping);

    }


    {
        Map<String, String> mpStamping = new LinkedHashMap<>();
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getDisplayNo), "显示顺序");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getMaterialNo), "零件号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getMaterialCn), "零件描述");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTemplateCode), "模板代码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTemplateName), "模板名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getProcessCode), "检测工位");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getProcessName), "检测工位名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getIsEnabled), "是否启用");
        // mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getIsRecheck), "是否需要复检");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getRemark), "模板备注");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getDisplayNo1), "检验项显示顺序");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemTypeName), "检验项类型名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getStandard), "检验标准");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemCode), "检测项编码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemName), "检测项名称");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute6), "级别码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute7  ), "特性码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute8), "特征码");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getItemTypeCode), "检验项类型");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute1), "特征号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute2), "首末孔");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute3), "轴号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute4), "刀具号");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute5), "量具");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getAttribute9), "特征类型");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTarget), "特征尺寸");
        // mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getTarget), "目标值");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getLowerLimit), "下限值");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getUpperLimit), "上限值");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getUnit), "单位");
        mpStamping.put(MpSqlUtils.getColumnName(PqsInspectionTemplateEntity::getRemark1), "备注");
        orderDicMa.put("检验模板", mpStamping);

    }

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsInspectionTemplateEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsInspectionTemplateEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsInspectionTemplateEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsInspectionTemplateEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 从缓存中获取检验模板信息
     *
     * @return 检验模板列表
     */
    @Override
    public List<PqsInspectionTemplateEntity> getAllDatas() {
        Function<Object, ? extends List<PqsInspectionTemplateEntity>> getDataFunc = (obj) -> {
            List<PqsInspectionTemplateEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsInspectionTemplateEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取检验模板  抽检/巡检使用
     *
     * @param entryType   工单类型
     * @param materialNo  物料号
     * @param processCode 工序
     * @return 模板列表
     */
    @Override
    public List<ComboInfoDTO> getTemplateList(String entryType, String materialNo, String processCode) {

        // 工序暂不使用
        return getAllDatas().stream().filter(q -> {
                    Boolean materialNo_ = StringUtils.equals(q.getMaterialNo(), StringUtils.EMPTY) || StringUtils.equals(q.getMaterialNo(), materialNo);
                    Boolean processCode_ = StringUtils.equals(q.getProcessCode(), StringUtils.EMPTY) || StringUtils.equals(q.getProcessCode(), processCode);
                    return q.getIsEnabled() && materialNo_ && processCode_;
                }).sorted(Comparator.comparing(PqsInspectionTemplateEntity::getDisplayNo))
                .map(c -> {
                    ComboInfoDTO dto = new ComboInfoDTO();
                    dto.setText(c.getTemplateName());
                    dto.setValue(c.getId().toString());
                    return dto;
                }).collect(Collectors.toList());
    }

    /**
     * 获取检验模板明细
     *
     * @param tempalteId 模板ID
     * @return 检验模板明细
     */
    @Override
    public List<PqsInspectionTemplateItemEntity> getTempalteDetail(String tempalteId) {
        QueryWrapper<PqsInspectionTemplateItemEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PqsInspectionTemplateItemEntity::getPrcPqsInspectionTemplateId, tempalteId);
        List<PqsInspectionTemplateItemEntity> data = pqsInspectionTemplateItemService.getData(queryWrapper, false);
        return data;
    }

    /**
     * 保存模板明细
     *
     * @param pqsInspectTemplateItemDtos 保存检验模板
     */
    @Override
    public void saveTemplateItem(List<PqsInspectionTemplateItemEntity> pqsInspectTemplateItemDtos) {
        UpdateWrapper<PqsInspectionTemplateItemEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PqsInspectionTemplateItemEntity::getPrcPqsInspectionTemplateId, pqsInspectTemplateItemDtos.get(0).getPrcPqsInspectionTemplateId());
        // modify by lee.li 因为没有正式环境没有物理删除权限，所以暂时修改为逻辑删除
        // pqsInspectionTemplateItemService.delete(updateWrapper, false);
        pqsInspectionTemplateItemService.delete(updateWrapper);
        pqsInspectionTemplateItemService.saveChange();

        PqsInspectionTemplateEntity entity = this.getAllDatas().stream().filter(c -> c.getId().equals(pqsInspectTemplateItemDtos.get(0).getPrcPqsInspectionTemplateId())).findFirst().orElse(null);
        if (entity != null) {
            List<PqsInspectionTemplateItemEntity> collect = pqsInspectTemplateItemDtos.stream().map(c -> {
                c.setId(Constant.DEFAULT_ID);
                return c;
            }).collect(Collectors.toList());
            pqsInspectionTemplateItemService.insertBatch(collect);
        }
    }

    @Override
    public void copyTemplate(TemplateCopyDto dto) {
        PqsInspectionTemplateEntity entity = this.get(dto.getId());
        if (entity != null) {
            PqsInspectionTemplateEntity newEntity = new PqsInspectionTemplateEntity();
            Long id = IdGenerator.getId();
            BeanUtils.copyProperties(entity, newEntity);
            newEntity.setId(id);
            newEntity.setTemplateName(dto.getTemplateName());
            PmProductMaterialMasterEntity temp = pmProductMaterialMasterService.getAllDatas().getData().stream().filter(c -> c.getMaterialNo().equals(dto.getMaterialNo())).findFirst().orElse(null);
            if (temp == null) {
                throw new InkelinkException("物料编码不存在");
            }
            newEntity.setMaterialNo(dto.getMaterialNo());
            newEntity.setMaterialCn(temp.getMaterialCn());
            newEntity.setTemplateCode(dto.getTemplateCode());

            List<PqsInspectionTemplateItemEntity> datas = pqsInspectionTemplateItemService.getAllDatas().stream().filter(c -> c.getPrcPqsInspectionTemplateId().equals(dto.getId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(datas)) {
                List<PqsInspectionTemplateItemEntity> newDatas = new LinkedList<>();
                datas.stream().forEach(x -> {
                    PqsInspectionTemplateItemEntity newItem = new PqsInspectionTemplateItemEntity();
                    BeanUtils.copyProperties(x, newItem);
                    newItem.setId(IdGenerator.getId());
                    newItem.setPrcPqsInspectionTemplateId(id);
                    newDatas.add(newItem);
                });
                this.pqsInspectionTemplateItemService.insertBatch(newDatas);
                this.pqsInspectionTemplateItemService.saveChange();
            }
            this.insert(newEntity);
            this.saveChange();
        }
    }

    @Override
    public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(orderDic.get("检验模板"));
        super.getImportTemplate(fileName, response);
    }

    @Override
    public void getImportTemplateMa(String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(orderDicMa.get("检验模板"));
        super.getImportTemplate(fileName, response);
    }

    @Override
    public void importExcel(InputStream is) throws Exception {
        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(is,
                orderDic.keySet().toArray(new String[orderDic.keySet().size()]));
        List<PqsInspectionTemplateItemEntity> itemEntityList = new ArrayList<>();
        List<PqsInspectionTemplateEntity> entityList = new ArrayList<>();
        List<PqsInspectionTemplateEntity> allDataLists = getAllDatas();
        for (Map.Entry<String, List<Map<String, String>>> item : datas.entrySet()) {
            if (item.getValue() == null || item.getValue().size() <= 0) {
                continue;
            }
            validImportDatas(item.getValue(), orderDic.get(item.getKey()));
            List<PqsInspectionTemplateEntity> dataEts = convertExcelDataToEntity(item.getValue());
            dataEts.forEach(u -> allDataLists.forEach(a -> {
                if (u.getTemplateCode().equals(a.getTemplateCode())) {
                    u.setId(a.getId());
                }
            }));

            Map<String, List<PqsInspectionTemplateEntity>> collect = dataEts.stream().collect(Collectors.groupingBy(c -> c.getDisplayNo() + "_" + c.getMaterialNo() + "_" + c.getMaterialCn() + "_" + c.getTemplateCode() + "_" + c.getTemplateName() + "_" + c.getEntryType() + "_" + c.getEntryTypeDesc() + "_" + c.getIsEnabled() + "_" + c.getRemark()));
            for (Map.Entry<String, List<PqsInspectionTemplateEntity>> entry : collect.entrySet()) {
                //组装模板表
                PqsInspectionTemplateEntity t = entry.getValue().get(0);
                PqsInspectionTemplateEntity templateEntity = new PqsInspectionTemplateEntity();
                templateEntity.setId(t.getId());
                templateEntity.setDisplayNo(t.getDisplayNo());
                templateEntity.setIsEnabled(t.getIsEnabled());
                templateEntity.setTemplateCode(t.getTemplateCode());
                templateEntity.setTemplateName(t.getTemplateName());
                templateEntity.setMaterialNo(t.getMaterialNo());
                templateEntity.setMaterialCn(t.getMaterialCn());
                templateEntity.setEntryType(t.getEntryType());
                templateEntity.setEntryTypeDesc(t.getEntryTypeDesc());
                templateEntity.setProcessCode(t.getProcessCode());
                templateEntity.setProcessName(t.getProcessName());
                templateEntity.setRemark(t.getRemark());

                entityList.add(templateEntity);
                //组装模板子表
                entry.getValue().stream().forEach(e -> {
                    PqsInspectionTemplateItemEntity build = new PqsInspectionTemplateItemEntity();
                    build.setId(IdGenerator.getId());
                    build.setPrcPqsInspectionTemplateId(templateEntity.getId());
                    build.setDisplayNo(e.getDisplayNo1());
                    build.setItemTypeCode(e.getItemTypeCode());
                    build.setItemTypeName(e.getItemTypeName());
                    build.setStandard(e.getStandard());
                    build.setItemCode(e.getItemCode());
                    build.setItemName(e.getItemName());
                    build.setTarget(e.getTarget());
                    build.setLowerLimit(e.getLowerLimit());
                    build.setUpperLimit(e.getUpperLimit());
                    build.setUnit(e.getUnit());
                    build.setRemark(e.getRemark1());
                    build.setCreatedBy(identityHelper.getUserId());
                    build.setLastUpdatedBy(identityHelper.getUserId());
                    itemEntityList.add(build);

                });
            }
        }
        if (!CollectionUtils.isEmpty(entityList)) {
            this.saveExcelData(entityList);
        }
        if (!CollectionUtils.isEmpty(itemEntityList)) {
            pqsInspectionTemplateItemService.saveExcelData(itemEntityList);
        }

    }


    @Override
    public void importExcelMa(InputStream is) throws Exception {
        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(is,
                orderDicMa.keySet().toArray(new String[orderDicMa.keySet().size()]));
        List<PqsInspectionTemplateItemEntity> itemEntityList = new ArrayList<>();
        List<PqsInspectionTemplateEntity> entityList = new ArrayList<>();
        List<PqsInspectionTemplateEntity> allDataLists = getAllDatas();
        for (Map.Entry<String, List<Map<String, String>>> item : datas.entrySet()) {
            if (item.getValue() == null || item.getValue().size() <= 0) {
                continue;
            }
            validImportDatas(item.getValue(), orderDicMa.get(item.getKey()));
            List<PqsInspectionTemplateEntity> dataEts = convertExcelDataToEntity(item.getValue());
            dataEts.forEach(u -> allDataLists.forEach(a -> {
                if (u.getTemplateCode().equals(a.getTemplateCode())) {
                    u.setId(a.getId());
                }
            }));

//            PqsInspectionTemplateEntity
            Map<String, List<PqsInspectionTemplateEntity>> collect = dataEts.stream().collect(Collectors.groupingBy(c -> c.getDisplayNo() + "_" + c.getMaterialNo() + "_" + c.getMaterialCn() + "_" + c.getTemplateCode() + "_" + c.getTemplateName() + "_" + c.getEntryType() + "_" + c.getEntryTypeDesc() + "_" + c.getIsEnabled() + "_" + c.getRemark()));
            for (Map.Entry<String, List<PqsInspectionTemplateEntity>> entry : collect.entrySet()) {
                //组装模板表
                PqsInspectionTemplateEntity t = entry.getValue().get(0);

                PqsInspectionTemplateEntity templateEntity = new PqsInspectionTemplateEntity();
                templateEntity.setId(t.getId());
                templateEntity.setDisplayNo(t.getDisplayNo());
                templateEntity.setIsEnabled(t.getIsEnabled());
                templateEntity.setTemplateCode(t.getTemplateCode());
                templateEntity.setTemplateName(t.getTemplateName());
                templateEntity.setMaterialNo(t.getMaterialNo());
                templateEntity.setMaterialCn(t.getMaterialCn());
                templateEntity.setEntryType(t.getEntryType());
                templateEntity.setEntryTypeDesc(t.getEntryTypeDesc());
                templateEntity.setProcessCode(t.getProcessCode());
                templateEntity.setProcessName(t.getProcessName());
                templateEntity.setRemark(t.getRemark());
                // 设置默认值MA
                templateEntity.setAttribute1("MA");
                entityList.add(templateEntity);
                //组装模板子表
                entry.getValue().stream().forEach(e -> {
                    PqsInspectionTemplateItemEntity build = new PqsInspectionTemplateItemEntity();
                    build.setId(IdGenerator.getId());
                    build.setPrcPqsInspectionTemplateId(templateEntity.getId());
                    build.setGroupName(e.getGroupName());
                    build.setDisplayNo(e.getDisplayNo1());
                    build.setItemTypeCode(e.getItemTypeCode());
                    build.setItemTypeName(e.getItemTypeName());
                    build.setStandard(e.getStandard());
                    build.setItemCode(e.getItemCode());
                    build.setItemName(e.getItemName());
                    build.setTarget(e.getTarget());
                    build.setLowerLimit(e.getLowerLimit());
                    build.setUpperLimit(e.getUpperLimit());
                    build.setUnit(e.getUnit());
                    build.setRemark(e.getRemark1());
                    build.setCreatedBy(identityHelper.getUserId());
                    build.setLastUpdatedBy(identityHelper.getUserId());

                    build.setAttribute1(e.getAttribute1());
                    build.setAttribute2(e.getAttribute2());
                    build.setAttribute3(e.getAttribute3());
                    build.setAttribute4(e.getAttribute4());
                    build.setAttribute5(e.getAttribute5());

                    build.setAttribute6(e.getAttribute6());
                    build.setAttribute7(e.getAttribute7());
                    build.setAttribute8(e.getAttribute8());
                    build.setAttribute9(e.getAttribute9());
                    itemEntityList.add(build);
                });
            }
        }
        if (!CollectionUtils.isEmpty(entityList)) {
            this.saveExcelData(entityList);
        }
        if (!CollectionUtils.isEmpty(itemEntityList)) {
            pqsInspectionTemplateItemService.saveExcelData(itemEntityList);
        }
    }

    @Override
    public void saveExcelData(List<PqsInspectionTemplateEntity> entities) {
        // excel数据入库，不需要数据验证
        entities.forEach(e -> e.setDataCheck(false));

        List<PqsInspectionTemplateEntity> allDataLists = getAllDatas();
        // 需要更新的数据
        List<PqsInspectionTemplateEntity> updateList = entities.stream()
                .filter(e -> allDataLists.contains(e)).collect(Collectors.toList());
        updateList.forEach(u -> allDataLists.forEach(a -> {
            if (u.getTemplateCode().equals(a.getTemplateCode())) {
                u.setId(a.getId());
            }
        }));

        // 需要新增的数据
        List<PqsInspectionTemplateEntity> insertList = entities.stream()
                .filter(e -> !updateList.contains(e)).collect(Collectors.toList());

        if (insertList.size() > 0) {
            insertBatch(insertList, insertList.size());
        }
        if (updateList.size() > 0) {
            updateBatchById(updateList, updateList.size());
        }
        removeCache();
        saveChange();
    }

    @Override
    public void beforeInsert(PqsInspectionTemplateEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsInspectionTemplateEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsInspectionTemplateEntity::getTemplateCode, entity.getTemplateCode());
            PqsInspectionTemplateEntity anomalyEntity = getData(queryWrapper, false).stream()
                    .findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getTemplateCode() + "已存在,请不要重复添加");
            }
        }
        if (StringUtils.isNotEmpty(entity.getMaterialNo())) {
            PmProductMaterialMasterEntity temp = pmProductMaterialMasterService.getAllDatas().getData().stream().
                    filter(c -> c.getMaterialNo().equals(entity.getMaterialNo())).findFirst().orElse(null);
            if (temp == null) {
                throw new InkelinkException("该【" + entity.getMaterialNo() + "】物料编码不存在");
            }
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(PqsInspectionTemplateEntity entity) {
        if (entity.isDataCheck()) {
            QueryWrapper<PqsInspectionTemplateEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PqsInspectionTemplateEntity::getTemplateCode, entity.getTemplateCode()).ne(PqsInspectionTemplateEntity::getId, entity.getId());
            PqsInspectionTemplateEntity anomalyEntity = getData(queryWrapper, false).stream().findFirst().orElse(null);
            if (anomalyEntity != null) {
                throw new InkelinkException("编码" + anomalyEntity.getTemplateCode() + "," + "已存在,请不要重复添加");
            }
        }
        if (StringUtils.isNotEmpty(entity.getMaterialNo())) {
            PmProductMaterialMasterEntity temp = pmProductMaterialMasterService.getAllDatas().getData().stream().filter(c -> c.getMaterialNo().equals(entity.getMaterialNo())).findFirst().orElse(null);
            if (temp == null) {
                throw new InkelinkException("该【" + entity.getMaterialNo() + "】物料编码不存在");
            }
        }
        removeCache();
    }

    @Override
    public List<PqsInspectionTemplateEntity> convertExcelDataToEntity(List<Map<String, String>> datas) throws Exception {
        List<PqsInspectionTemplateEntity> entities = new ArrayList<>();
        for (Map<String, String> map : datas) {
            if ("是".equals(map.get("isEnabled"))) {
                map.put("isEnabled", "1");
            } else if ("否".equals(map.get("isEnabled"))) {
                map.put("isEnabled", "0");
            }
            if ("是".equals(map.get("isRecheck"))) {
                map.put("isRecheck", "1");
            } else if ("否".equals(map.get("isRecheck"))) {
                map.put("isRecheck", "0");
            }
            PqsInspectionTemplateEntity entitie = BeanUtil.fillBeanWithMap(map, this.currentModelClass().newInstance(), false);
            setField(entitie, currentModelKeyProperty(), IdGenerator.getId());
            setField(entitie, "lastUpdateDate", new Date());
            setField(entitie, "lastUpdatedBy", identityHelper.getUserId());
            setField(entitie, "lastUpdatedUser", identityHelper.getUserName());
            setField(entitie, "createdBy", identityHelper.getUserId());
            setField(entitie, "createdUser", identityHelper.getUserName());
            setField(entitie, "creationDate", new Date());
            ClassUtil.defaultValue(entitie);
            entities.add(entitie);
        }
        return entities;
    }

    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(orderDic.get("检验模板"));
        Map<String, String> fieldParam = getExcelColumnNames();
        List<PqsInspectionTemplateEntity> datas = pqsInspectionTemplateMapper.getExcelData();
        List<Map<String, Object>> mapList = InkelinkExcelUtils.getListMap(datas);
        dealExcelDatas(mapList);
        InkelinkExcelUtils.exportByDc(fieldParam, mapList, fileName, response);
    }


    @Override
    public void exportMa(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(orderDicMa.get("检验模板"));
        Map<String, String> fieldParam = getExcelColumnNames();
        List<PqsInspectionTemplateEntity> datas = pqsInspectionTemplateMapper.getExcelDataMa();
        List<Map<String, Object>> mapList = InkelinkExcelUtils.getListMap(datas);
        dealExcelDatas(mapList);
        InkelinkExcelUtils.exportByDc(fieldParam, mapList, fileName, response);
    }


    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if ("true".equals(data.get("isEnabled"))) {
                data.put("isEnabled", "是");
            } else if ("false".equals(data.get("isEnabled"))) {
                data.put("isEnabled", "否");
            }
            if ("true".equals(data.get("isRecheck"))) {
                data.put("isRecheck", "是");
            } else if ("false".equals(data.get("isRecheck"))) {
                data.put("isRecheck", "否");
            }
        }
    }
}