package com.ca.mfd.prc.core.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.dc.dto.FieldBatchPara;
import com.ca.mfd.prc.core.prm.entity.DcFieldConfigEntity;
import com.ca.mfd.prc.core.prm.mapper.IDcFieldConfigMapper;
import com.ca.mfd.prc.core.dc.service.IDcFieldConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class IDcFieldConfigServiceImpl extends AbstractCrudServiceImpl<IDcFieldConfigMapper, DcFieldConfigEntity> implements IDcFieldConfigService {

    @Override
    public List<DcFieldConfigEntity> getFieldListByPage(Long id) {
        QueryWrapper<DcFieldConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<DcFieldConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(DcFieldConfigEntity::getPrcDcPageConfigId, id);
        return selectList(queryWrapper);
    }

    @Override
    public void batchSet(FieldBatchPara para) {
        DcFieldConfigEntity configEntity = para.getFields().stream().filter(p -> p.getFiledPropertyName().toLowerCase().contains("id")).findFirst().orElse(null);
        if (configEntity == null) {
            throw new InkelinkException("必须有主键ID字段");
        }
        //先删除
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("PRC_DC_PAGE_CONFIG_ID", para.getPageId().toString(), ConditionOper.Equal));
        delete(conditionDtos, true);
        //批量新增
        para.getFields().stream().forEach(s -> {
            s.setId(Constant.DEFAULT_ID);
            // 特殊处理null
            if (s.getConditionFiled() == null) {
                s.setConditionFiled(StringUtils.EMPTY);
            }
        });
        insertBatch(para.getFields());
        saveChange();
    }

    @Override
    public List<DcFieldConfigEntity> getPageFieldList(Long pageId) {
        QueryWrapper<DcFieldConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(DcFieldConfigEntity::getPrcDcPageConfigId, pageId);
        List<DcFieldConfigEntity> list;
        list = selectList(qry).stream().sorted(Comparator.comparing(DcFieldConfigEntity::getListDisplayNo)).collect(Collectors.toList());
        if (list.size() > 0) {
            return list;
        }
        //赋值
        DcFieldConfigEntity entity = new DcFieldConfigEntity();
        entity.setPrcDcPageConfigId(pageId);
        entity.setFiledName("主键");
        entity.setFiledPropertyName("Id");
        entity.setFiledType("string");
        entity.setFiledConvertSource(StringUtils.EMPTY);
        entity.setIsShowList(false);
        entity.setListDisplayNo(0);
        entity.setIsShowUpdate(false);
        entity.setVerifyExpression(StringUtils.EMPTY);
        entity.setRenderder(StringUtils.EMPTY);
        entity.setUpdateDisplayNo(0);
        entity.setIsShowAdd(false);
        entity.setAddDisplayNo(0);
        entity.setIsShowSelect(false);
        entity.setSelectDisplayNo(0);
        entity.setIsSelectRange(false);
        list.add(entity);


        //赋值
        entity = new DcFieldConfigEntity();
        entity.setPrcDcPageConfigId(pageId);
        entity.setFiledName("更新人");
        entity.setFiledPropertyName("Updater");
        entity.setFiledType("string");
        entity.setFiledConvertSource(StringUtils.EMPTY);
        entity.setIsShowList(false);
        entity.setListDisplayNo(80);
        entity.setIsShowUpdate(false);
        entity.setVerifyExpression(StringUtils.EMPTY);
        entity.setRenderder(StringUtils.EMPTY);
        entity.setUpdateDisplayNo(0);
        entity.setIsShowAdd(false);
        entity.setAddDisplayNo(0);
        entity.setIsShowSelect(false);
        entity.setSelectDisplayNo(0);
        entity.setIsSelectRange(false);
        list.add(entity);


        //赋值
        entity = new DcFieldConfigEntity();
        entity.setPrcDcPageConfigId(pageId);
        entity.setFiledName("创建人");
        entity.setFiledPropertyName("Creater");
        entity.setFiledType("string");
        entity.setFiledConvertSource(StringUtils.EMPTY);
        entity.setIsShowList(false);
        entity.setListDisplayNo(81);
        entity.setIsShowUpdate(false);
        entity.setVerifyExpression(StringUtils.EMPTY);
        entity.setRenderder(StringUtils.EMPTY);
        entity.setUpdateDisplayNo(0);
        entity.setIsShowAdd(false);
        entity.setAddDisplayNo(0);
        entity.setIsShowSelect(false);
        entity.setSelectDisplayNo(0);
        entity.setIsSelectRange(false);
        list.add(entity);

        //赋值
        entity = new DcFieldConfigEntity();
        entity.setPrcDcPageConfigId(pageId);
        entity.setFiledName("更新时间");
        entity.setFiledPropertyName("UpdateDt");
        entity.setFiledType("datetime");
        entity.setFiledConvertSource(StringUtils.EMPTY);
        entity.setIsShowList(false);
        entity.setListDisplayNo(82);
        entity.setIsShowUpdate(false);
        entity.setVerifyExpression(StringUtils.EMPTY);
        entity.setRenderder(StringUtils.EMPTY);
        entity.setUpdateDisplayNo(0);
        entity.setIsShowAdd(false);
        entity.setAddDisplayNo(0);
        entity.setIsShowSelect(false);
        entity.setSelectDisplayNo(0);
        entity.setIsSelectRange(false);
        list.add(entity);


        //赋值
        entity = new DcFieldConfigEntity();
        entity.setPrcDcPageConfigId(pageId);
        entity.setFiledName("创建时间");
        entity.setFiledPropertyName("CreateDt");
        entity.setFiledType("datetime");
        entity.setFiledConvertSource(StringUtils.EMPTY);
        entity.setIsShowList(false);
        entity.setListDisplayNo(83);
        entity.setIsShowUpdate(false);
        entity.setVerifyExpression(StringUtils.EMPTY);
        entity.setRenderder(StringUtils.EMPTY);
        entity.setUpdateDisplayNo(0);
        entity.setIsShowAdd(false);
        entity.setAddDisplayNo(0);
        entity.setIsShowSelect(false);
        entity.setSelectDisplayNo(0);
        entity.setIsSelectRange(false);
        list.add(entity);

        return list;
    }
}
