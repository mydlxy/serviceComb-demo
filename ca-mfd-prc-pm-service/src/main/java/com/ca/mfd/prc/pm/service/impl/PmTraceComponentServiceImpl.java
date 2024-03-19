package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmTraceComponentEntity;
import com.ca.mfd.prc.pm.entity.PmTraceComponentMaterialEntity;
import com.ca.mfd.prc.pm.mapper.IPmTraceComponentMapper;
import com.ca.mfd.prc.pm.service.IPmTraceComponentMaterialService;
import com.ca.mfd.prc.pm.service.IPmTraceComponentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: 追溯组件配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmTraceComponentServiceImpl extends AbstractCrudServiceImpl<IPmTraceComponentMapper, PmTraceComponentEntity> implements IPmTraceComponentService {


    private final String cacheName = "PRC_PM_TRACE_COMPONENT";
    @Autowired
    private LocalCache localCache;


    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmTraceComponentEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmTraceComponentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmTraceComponentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmTraceComponentEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PmTraceComponentEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PmTraceComponentEntity entity) {
        verfyOperation(entity, true);
    }

    /**
     * 验证方法
     */
    void verfyOperation(PmTraceComponentEntity model, boolean isUpdate) {

       /* List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("LINE_CODE", model.getLineCode(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PmTraceComponentEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("线体编码" + model.getLineCode() + "已经存在，不允许重复录入！");
        }*/
    }
    
    /**
     * 获取所有的数据
     *
     * @return List<PmTraceComponentEntity>
     */
    @Override
    public List<PmTraceComponentEntity> getAllDatas() {
        Function<Object, ? extends List<PmTraceComponentEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, 6000);
    }


    @Autowired
    private IPmTraceComponentMaterialService pmTraceComponentMaterialService;


    @Override
    public PmTraceComponentEntity getByCode(String code) {
        QueryWrapper<PmTraceComponentEntity> qry = new QueryWrapper<>();
        qry.eq("TRACE_COMPONENT_CODE",code);
        return selectList(qry).stream().findFirst().orElse(null);
    }

    public void saveByBom(String  materialNo,String materialCn) {
        if(StringUtils.isBlank(materialCn)){
            materialCn = "未知";
        }
        PmProductBomEntity bom = new PmProductBomEntity();
        bom.setMaterialNo(materialNo);
        bom.setMaterialCn(materialCn);
        List<PmProductBomEntity> boms = new ArrayList<>();
        boms.add(bom);
        saveByBom(boms);
    }

    public void saveByBom(List<PmProductBomEntity> boms) {
        if (boms == null || boms.isEmpty()) {
            return;
        }
        for (PmProductBomEntity bom : boms) {
            if (StringUtils.isBlank(bom.getMaterialNo()) || bom.getMaterialNo().length() < 7) {
                continue;
            }
            bom.setAttribute3(bom.getMaterialNo().trim().substring(0, 7));
        }
        Map<String, List<PmProductBomEntity>> gps = boms.stream().filter(c -> !StringUtils.isBlank(c.getAttribute3()))
                .collect(Collectors.groupingBy(c -> c.getAttribute3()));

        //List<PmTraceComponentEntity> datas = getAllDatas();
        List<PmTraceComponentEntity> newDatas = new ArrayList<>();
        List<PmTraceComponentMaterialEntity> details = new ArrayList<>();
        for (Map.Entry<String, List<PmProductBomEntity>> ent : gps.entrySet()) {
            String comKey = ent.getKey();
            PmProductBomEntity firstEnt = ent.getValue().get(0);
            QueryWrapper<PmTraceComponentEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PmTraceComponentEntity::getTraceComponentCode, comKey);
            PmTraceComponentEntity item = getTopDatas(1, qry)
                    .stream().findFirst().orElse(null);
            Long pmTraceComponentId = 0L;
            if (item == null) {
                pmTraceComponentId = IdGenerator.getId();
                item = new PmTraceComponentEntity();
                item.setId(pmTraceComponentId);
                /*3CJ   3C件
                YBJ 一般件
                ZYJ 重要件
                GJJ 关键件
                QT 其他*/
                item.setCategory("QT");
                item.setTraceComponentCode(comKey);
                item.setTraceComponentDescription(firstEnt.getMaterialCn());
                //this.insert(item);
                newDatas.add(item);
                //

            } else {
                pmTraceComponentId = item.getId();
            }
            for (PmProductBomEntity bomData : ent.getValue()) {
                PmTraceComponentMaterialEntity comMaterial = pmTraceComponentMaterialService.getFirstByMaterialNo(bomData.getMaterialNo());
                if (comMaterial == null) {
                    PmTraceComponentMaterialEntity cmDe = new PmTraceComponentMaterialEntity();
                    cmDe.setPrcPmTraceComponentId(pmTraceComponentId);
                    cmDe.setMaterialNo(bomData.getMaterialNo());
                    cmDe.setRemark(bomData.getMaterialCn());
                    details.add(cmDe);
                }
            }

        }
        if (!newDatas.isEmpty()) {
            this.insertBatch(newDatas);
        }
        if (!details.isEmpty()) {
            pmTraceComponentMaterialService.insertBatch(details.stream().distinct().collect(Collectors.toList()),200,false,1);
        }

    }
}