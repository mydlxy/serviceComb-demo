package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteAreaEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviRouteAreaMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteAreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: 路由区服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcAviRouteAreaServiceImpl extends AbstractCrudServiceImpl<IRcAviRouteAreaMapper, RcAviRouteAreaEntity> implements IRcAviRouteAreaService {


    @Override
    public void beforeUpdate(RcAviRouteAreaEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcAviRouteAreaEntity model) {
        validData(model);
    }

    private void validData(RcAviRouteAreaEntity model) {
        if (StringUtils.isBlank(model.getAreaName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "AreaName", model.getAreaName(), "已经存在名称为%s的数据", "", "");
        RcAviRouteAreaEntity data = getAreaEntityCode(model.getAreaCode(), model.getId());
        if (data != null) {
            throw new InkelinkException("已经存在代码为" + model.getAreaCode() + "路由区");
        }
    }

    private RcAviRouteAreaEntity getAreaEntityCode(String code, Long id) {
        QueryWrapper<RcAviRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcAviRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcAviRouteAreaEntity::getAreaCode, code);
        lambdaQueryWrapper.ne(RcAviRouteAreaEntity::getId, id);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据 主键集合  查询 路由区列表
     *
     * @param areaIds 主键集合
     * @return 路由区列表
     */
    @Override
    public List<RcAviRouteAreaEntity> getAreaEntityByIds(Long[] areaIds) {
        QueryWrapper<RcAviRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcAviRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RcAviRouteAreaEntity::getId, Arrays.asList(areaIds));
        return selectList(queryWrapper);
    }
}