package com.ca.mfd.prc.core.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.dto.ButtonBatchSetPara;
import com.ca.mfd.prc.core.prm.entity.DcButtonConfigEntity;
import com.ca.mfd.prc.core.prm.mapper.IDcButtonConfigMapper;
import com.ca.mfd.prc.core.dc.service.IDcButtonConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 按钮配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class IDcButtonConfigServiceImpl extends AbstractCrudServiceImpl<IDcButtonConfigMapper, DcButtonConfigEntity> implements IDcButtonConfigService {

    @Override
    public List<DcButtonConfigEntity> getButtonListByPage(Long id) {
        QueryWrapper<DcButtonConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<DcButtonConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(DcButtonConfigEntity::getPrcDcPageConfigId, id);
        lambdaQueryWrapper.orderByAsc(DcButtonConfigEntity::getDisplayNo);
        return this.selectList(queryWrapper);
    }

    @Override
    public void batchSet(ButtonBatchSetPara para) {
        //先删除
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("PRC_DC_PAGE_CONFIG_ID", para.getPageId(), ConditionOper.Equal));
        delete(conditionDtos, true);
        //批量新增
        if (para.getButtons().size() > 0) {
            para.getButtons().stream().forEach(s->{
                s.setId(Constant.DEFAULT_ID);
            });
            insertBatch(para.getButtons());
        }
        saveChange();
    }

    @Override
    public List<DcButtonConfigEntity> getPageButtonList(Long pageId) {
        QueryWrapper<DcButtonConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(DcButtonConfigEntity::getPrcDcPageConfigId, pageId);
        List<DcButtonConfigEntity> list = selectList(qry).stream().sorted(Comparator.comparing(DcButtonConfigEntity::getDisplayNo)).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<DcButtonConfigEntity> getListByCode(String code) {
        QueryWrapper<DcButtonConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(DcButtonConfigEntity::getAuthorizationCode, code);
        return selectList(qry);
    }
}
