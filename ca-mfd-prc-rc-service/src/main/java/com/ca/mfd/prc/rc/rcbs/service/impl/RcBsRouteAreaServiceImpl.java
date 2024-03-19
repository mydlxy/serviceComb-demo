package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteAreaItemVO;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteAreaEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRouteAreaMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteAreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 路由区服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcBsRouteAreaServiceImpl extends AbstractCrudServiceImpl<IRcBsRouteAreaMapper, RcBsRouteAreaEntity> implements IRcBsRouteAreaService {


    @Override
    public void beforeUpdate(RcBsRouteAreaEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBsRouteAreaEntity model) {
        validData(model);
    }

    private void validData(RcBsRouteAreaEntity model) {
        if (model.getAreaCode() <= 0) {
            throw new InkelinkException("代码不能小于等于0");
        }
        if (StringUtils.isBlank(model.getAreaName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "AreaName", model.getAreaName(), "已经存在名称为%s的数据", "", "");
        RcBsRouteAreaEntity data = getAreaEntityCode(model.getAreaCode(), model.getBufferCode(), model.getId());
        if (data != null) {
            throw new InkelinkException("缓存区:" + model.getBufferCode() + "已经存在代码为" + model.getAreaCode() + "路由区");
        }
    }

    private RcBsRouteAreaEntity getAreaEntityCode(int areaCode, String bufferCode, Long id) {
        QueryWrapper<RcBsRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteAreaEntity::getAreaCode, areaCode);
        lambdaQueryWrapper.eq(RcBsRouteAreaEntity::getBufferCode, bufferCode);
        lambdaQueryWrapper.ne(RcBsRouteAreaEntity::getId, id);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据 主键集合  查询 路由区列表
     *
     * @param areaIds 主键集合
     * @return 路由区列表
     */
    @Override
    public List<RcBsRouteAreaEntity> getAreaEntityByIds(Long[] areaIds) {
        QueryWrapper<RcBsRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RcBsRouteAreaEntity::getId, Arrays.asList(areaIds));
        return selectList(queryWrapper);
    }

    /**
     * 根据路由区ID查询
     *
     * @param areaId 路由区ID
     * @return 列表
     */
    @Override
    public RcRouteAreaItemVO getAreaEntityByAreaId(Long areaId) {
        QueryWrapper<RcBsRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteAreaEntity::getId, areaId);
        List<RcBsRouteAreaEntity> list = selectList(queryWrapper);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.stream().map(s -> {
            RcRouteAreaItemVO info = new RcRouteAreaItemVO();
            info.setId(s.getId());
            info.setAreaCode(s.getAreaCode());
            info.setAreaName(s.getAreaName());
            return info;
        }).collect(Collectors.toList()).stream().findFirst().orElse(null);
    }
}