package com.ca.mfd.prc.pm.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.dto.TextAndValueMappingDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmBopGroupEntity;
import com.ca.mfd.prc.pm.mapper.IPmBopGroupMapper;
import com.ca.mfd.prc.pm.service.IPmBopGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @Description: 分组配置服务实现
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Service
public class PmBopGroupServiceImpl extends AbstractCrudServiceImpl<IPmBopGroupMapper, PmBopGroupEntity> implements IPmBopGroupService {

    @Override
    public List<TextAndValueMappingDTO> getPmCombo(String workshopCode){
        QueryWrapper<PmBopGroupEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopGroupEntity> lqw =  qw.lambda();
        if(StringUtils.isNotBlank(workshopCode)){
            lqw.eq(PmBopGroupEntity::getWorkshopCode,workshopCode);
        }
        List<PmBopGroupEntity> bopGroups = this.getData(qw,false);
        if(bopGroups.isEmpty()){
            return Collections.emptyList();
        }
        return bopGroups.stream().map(bopGroup -> new TextAndValueMappingDTO(bopGroup.getGroupCode(),bopGroup.getGroupName())).collect(Collectors.toList());
    }

    @Override
    public void beforeUpdate(PmBopGroupEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(PmBopGroupEntity model) {
        validData(model);
    }

    private void validData(PmBopGroupEntity model){
        validDataUnique(model.getId(), "GROUP_CODE", model.getGroupCode(), "分组编码为%s的数据,已经存在","PRC_PM_WORKSHOP_CODE",model.getWorkshopCode());
    }
}