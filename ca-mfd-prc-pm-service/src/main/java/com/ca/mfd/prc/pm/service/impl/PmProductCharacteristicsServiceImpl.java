package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.dto.ProductCharacteristicsDTO;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pm.mapper.IPmProductCharacteristicsMapper;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 特征主数据
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmProductCharacteristicsServiceImpl extends AbstractCrudServiceImpl<IPmProductCharacteristicsMapper, PmProductCharacteristicsEntity> implements IPmProductCharacteristicsService {

    @Override
    public List<PmProductCharacteristicsEntity> getByCharacteristicsVersionsId(Long characteristicsVersionsId) {
        QueryWrapper<PmProductCharacteristicsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductCharacteristicsEntity::getCharacteristicsVersionsId, characteristicsVersionsId);
        return selectList(qry);
    }


    @Override
    public PmProductCharacteristicsEntity getByVersionsAndName(String versionsid, String name, String materialNo, String isdelete) {
        QueryWrapper<PmProductCharacteristicsEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmProductCharacteristicsEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmProductCharacteristicsEntity::getCharacteristicsVersionsId, versionsid);
        lambdaQueryWrapper.eq(PmProductCharacteristicsEntity::getProductCharacteristicsName, name);
        lambdaQueryWrapper.eq(PmProductCharacteristicsEntity::getMaterialNo, materialNo);
        lambdaQueryWrapper.eq(PmProductCharacteristicsEntity::getIsDelete, isdelete);
        return this.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public List<ProductCharacteristicsDTO> getCharacteristicsMaster() {
        QueryWrapper<PmProductCharacteristicsEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmProductCharacteristicsEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.orderByAsc(PmProductCharacteristicsEntity::getProductCharacteristicsName);
        List<PmProductCharacteristicsEntity> productCharacteristicsList = this.selectList(queryWrapper);
        if (productCharacteristicsList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProductCharacteristicsDTO> targetList = new ArrayList<>(productCharacteristicsList.size());
        for (PmProductCharacteristicsEntity pmProductCharacteristicsEntity : productCharacteristicsList) {
            ProductCharacteristicsDTO productCharacteristicsDTO = new ProductCharacteristicsDTO();
            productCharacteristicsDTO.setFeatureName(pmProductCharacteristicsEntity.getProductCharacteristicsName());
            productCharacteristicsDTO.setValueDescription(pmProductCharacteristicsEntity.getValueCn());
            productCharacteristicsDTO.setValue(pmProductCharacteristicsEntity.getProductCharacteristicsValue());
            productCharacteristicsDTO.setFeatureDescription(pmProductCharacteristicsEntity.getDescriptionCn());
            targetList.add(productCharacteristicsDTO);
        }
        return targetList;
    }
}