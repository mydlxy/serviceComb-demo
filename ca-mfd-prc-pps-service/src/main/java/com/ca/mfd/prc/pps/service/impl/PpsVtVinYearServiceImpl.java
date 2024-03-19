package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.pps.entity.PpsVtVinYearEntity;
import com.ca.mfd.prc.pps.entity.PpsVtVinYearEntity;
import com.ca.mfd.prc.pps.mapper.IPpsVtVinYearMapper;
import com.ca.mfd.prc.pps.service.IPpsVtVinYearService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * VIN号年份配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsVtVinYearServiceImpl extends AbstractCrudServiceImpl<IPpsVtVinYearMapper, PpsVtVinYearEntity> implements IPpsVtVinYearService {

    private final Map<String, String> orderDic = new LinkedHashMap<>();

    {
        orderDic.put(MpSqlUtils.getColumnName(PpsVtVinYearEntity::getYear), "年份");
        orderDic.put(MpSqlUtils.getColumnName(PpsVtVinYearEntity::getVinYearCode), "代码");
    }

    @Override
    public void afterInsert(PpsVtVinYearEntity model) {
        QueryWrapper<PpsVtVinYearEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsVtVinYearEntity::getYear, model.getYear());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("年份["+model.getYear()+"]已存在配置");
        }
    }

    @Override
    public void afterUpdate(PpsVtVinYearEntity model) {
        QueryWrapper<PpsVtVinYearEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsVtVinYearEntity::getYear, model.getYear())
                .ne(PpsVtVinYearEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("年份["+model.getYear()+"]已存在配置");
        }
    }

    /**
     * 根据年份查询
     *
     * @param year 查询年份
     * @return 实体
     */
    @Override
    public PpsVtVinYearEntity getEntityByYear(String year) {
        QueryWrapper<PpsVtVinYearEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsVtVinYearEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsVtVinYearEntity::getYear, year);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        return orderDic;
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        //验证必填
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsVtVinYearEntity::getYear), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsVtVinYearEntity::getVinYearCode), i + 1);
        }
    }

    void validExcelDataRequire(Map<String, String> fieldParam, Map<String, String> data, String col, int rowIndex) {
        String columnName = fieldParam.get(col);
        String val = data.get(col);
        if (StringUtils.isBlank(val)) {
            throw new InkelinkException("第“" + rowIndex + "”行，“" + columnName + "”列：不能为空");
        }
    }

    @Override
    public void saveExcelData(List<PpsVtVinYearEntity> entities) {
        String years = String.join("|", entities.stream().map(o -> o.getYear().toString()).collect(Collectors.toList()));
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("YEAR", years, ConditionOper.In));
        delete(conditionInfos);
        insertBatch(entities);
        saveChange();
    }
}