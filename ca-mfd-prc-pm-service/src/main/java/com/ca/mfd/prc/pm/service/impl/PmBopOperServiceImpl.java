package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.dto.TextAndValueMappingDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;

import com.ca.mfd.prc.pm.entity.PmBopGroupEntity;
import com.ca.mfd.prc.pm.entity.PmBopOperEntity;
import com.ca.mfd.prc.pm.mapper.IPmBopOperMapper;
import com.ca.mfd.prc.pm.service.IPmBopOperService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @Description: BOP操作服务实现
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Service
public class PmBopOperServiceImpl extends AbstractCrudServiceImpl<IPmBopOperMapper, PmBopOperEntity> implements IPmBopOperService {
    @Override
    public void beforeUpdate(PmBopOperEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(PmBopOperEntity model) {
        validData(model);
    }

    private void validData(PmBopOperEntity model){
        validDataUnique(model.getId(), "OPER_CODE", model.getOperCode(), "操作编码为%s的数据,已经存在");
    }

    @Override
    public List<TextAndValueMappingDTO> getPmCombo(String workShopCode,String groupCode){
        QueryWrapper<PmBopOperEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopOperEntity> lqw =  qw.lambda();
        if(StringUtils.isNotBlank(workShopCode)){
            lqw.eq(PmBopOperEntity::getWorkshopCode,workShopCode);
        }
        if(StringUtils.isNotBlank(groupCode)){
            lqw.eq(PmBopOperEntity::getGroupCode,groupCode);
        }
        List<PmBopOperEntity> BopOpers = this.getData(qw,false);
        if(BopOpers.isEmpty()){
            return Collections.emptyList();
        }
        return BopOpers.stream().map(bopGroup -> new TextAndValueMappingDTO(bopGroup.getOperCode(),bopGroup.getOperName())).collect(Collectors.toList());
    }

}