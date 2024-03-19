package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmReactionPlanEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.mapper.IPmReactionPlanMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkShopMapper;
import com.ca.mfd.prc.pm.service.IPmReactionPlanService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 * @Description: 反应计划服务实现
 * @author inkelink
 * @date 2023年12月29日
 * @变更说明 BY inkelink At 2023年12月29日
 */
@Service
public class PmReactionPlanServiceImpl extends AbstractCrudServiceImpl<IPmReactionPlanMapper, PmReactionPlanEntity> implements IPmReactionPlanService {

    @Autowired
    private IPmWorkShopMapper pmWorkShopMapper;

    @Override
    public void beforeUpdate(PmReactionPlanEntity entity) {
        valid(entity);
    }

    @Override
    public void beforeInsert(PmReactionPlanEntity entity) {
        valid(entity);
    }

    private void valid(PmReactionPlanEntity entity) {
        //车间信息
        validWorkShop(entity);
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new LinkedHashMap<>();
            columnNames.put("workshopCode", "车间编码");
            columnNames.put("planCode", "编码");
            columnNames.put("planStep", "反应步骤");
            columnNames.put("planDesc", "反应步骤描述");
            columnNames.put("remark", "备注");
        return columnNames;
    }

    private void validWorkShop(PmReactionPlanEntity entity) {
        //车间信息
        QueryWrapper<PmWorkShopEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkShopEntity> lwq = qw.lambda();
        if(entity.getPrcPmWorkshopId() != null && entity.getPrcPmWorkshopId() > 0){
            lwq.eq(PmWorkShopEntity::getId, entity.getPrcPmWorkshopId());
        }else if(StringUtils.isNotBlank(entity.getWorkshopCode())){
            lwq.eq(PmWorkShopEntity::getWorkshopCode, entity.getWorkshopCode());
        }else{
            throw new InkelinkException("车间id和车间编码不能同时为空");
        }
        lwq.eq(PmWorkShopEntity::getVersion, 0);
        lwq.eq(PmWorkShopEntity::getIsDelete, false);
        PmWorkShopEntity pmWorkShopEntity = pmWorkShopMapper.selectList(qw).stream().findFirst().orElse(null);
        if (pmWorkShopEntity == null) {
            throw new InkelinkException(String.format("反应计划对应的车间[%s]不存在",entity.getPrcPmWorkshopId()));
        }
        entity.setWorkshopCode(pmWorkShopEntity.getWorkshopCode());
        entity.setPrcPmWorkshopId(pmWorkShopEntity.getId());
    }

}