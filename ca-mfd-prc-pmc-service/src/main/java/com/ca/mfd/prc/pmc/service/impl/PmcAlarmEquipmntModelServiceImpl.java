package com.ca.mfd.prc.pmc.service.impl;

import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntModelEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcAlarmEquipmntModelMapper;
import com.ca.mfd.prc.pmc.service.IPmcAlarmEquipmntModelService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备建模
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class PmcAlarmEquipmntModelServiceImpl extends AbstractCrudServiceImpl<IPmcAlarmEquipmntModelMapper, PmcAlarmEquipmntModelEntity> implements IPmcAlarmEquipmntModelService {
    private static final String cacheName = "PRC_PMC_ALARM_EQUIPMNT_MODEL";
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;

    @Override
    public void beforeInsert(PmcAlarmEquipmntModelEntity model) {
        List<PmcAlarmEquipmntModelEntity> list = getData(null);
        if (list.stream().filter(c -> c.getPosition().equals(model.getPosition())).collect(Collectors.toList()).size() > 0) {
            throw new InkelinkException("位置已被使用");
        }
    }

    @Override
    public void beforeUpdate(PmcAlarmEquipmntModelEntity model) {
        List<PmcAlarmEquipmntModelEntity> list = getData(null);
        if (list.stream().filter(c -> c.getPosition().equals(model.getPosition()) && !c.getId().equals(model.getId())).collect(Collectors.toList()).size() > 0) {
            throw new InkelinkException("位置已被使用");
        }
    }

    @Override
    public List<PmcAlarmEquipmntModelEntity> getAllDatas() {
        List<PmcAlarmEquipmntModelEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas, 60 * 10);
                }
            }
        }
        return datas;
    }

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmcAlarmEquipmntModelEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmcAlarmEquipmntModelEntity model) {
        removeCache();
    }

    @Override
    public List<ComboInfoDTO> getAlarmEquipmntLevelOne(String shopCode) {
        List<ComboInfoDTO> datas;
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("level", "1", ConditionOper.Equal));
        if (!Strings.isNullOrEmpty(shopCode)) {
            conditionDtos.add(new ConditionDto("workshopCode", shopCode, ConditionOper.Equal));
        }
        datas = this.getData(conditionDtos).stream().map(t -> {
            ComboInfoDTO dto = new ComboInfoDTO();
            dto.setText(t.getAlarmEquipmntModelCode());
            dto.setValue(t.getPosition());
            return dto;
        }).collect(Collectors.toList());

        return datas;
    }

    @Override
    public List<PmcAlarmEquipmntModelEntity> getAlarmEquipmntModelByShop(String shop, String key) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        ConditionDto condition;
        if (shop.length() > 0) {
            condition = new ConditionDto();
            condition.setColumnName("WORKSHOP_CODE");
            condition.setValue(shop);
            condition.setGroup("T1");
            condition.setGroupRelation(ConditionRelation.And);
            conditionInfos.add(condition);
        }

        if (StringUtils.isNotBlank(key)) {
            condition = new ConditionDto();
            condition.setColumnName("ALARM_EQUIPMNT_MODEL_NAME");
            condition.setOperator(ConditionOper.AllLike);
            condition.setValue(key);
            condition.setRelation(ConditionRelation.Or);
            condition.setGroup("T2");
            conditionInfos.add(condition);

            condition = new ConditionDto();
            condition.setColumnName("POSITION");
            condition.setOperator(ConditionOper.AllLike);
            condition.setValue(key);
            condition.setRelation(ConditionRelation.Or);
            condition.setGroup("T2");
            conditionInfos.add(condition);

            return this.getData(conditionInfos);
        }

        return this.getTopDatas(20, conditionInfos, null);
    }


}
