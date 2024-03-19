package com.ca.mfd.prc.bdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.bdc.dto.RcRouteAreaItemVO;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteLaneEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRouteAreaMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteAreaService;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteLaneService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 路由区服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRouteAreaServiceImpl extends AbstractCrudServiceImpl<IRcBdcRouteAreaMapper, RcBdcRouteAreaEntity> implements IRcBdcRouteAreaService {

    @Autowired
    IRcBdcRouteLaneService rcBdcRouteLaneService;


    @Override
    public void beforeUpdate(RcBdcRouteAreaEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBdcRouteAreaEntity model) {
        validData(model);
    }

    private void validData(RcBdcRouteAreaEntity model) {

        if (model.getAreaCode() <= 0) {
            throw new InkelinkException("代码不能小于等于0");
        }
        if (StringUtils.isBlank(model.getAreaName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "AreaName", model.getAreaName(), "已经存在名称为%s的数据", "", "");
        RcBdcRouteAreaEntity data = getDataByAreaCodeAndBufferCode(model.getAreaCode(), model.getBufferCode(), model.getId());
        if (data != null) {
            throw new InkelinkException("缓存区" + model.getBufferCode() + "已经存在代码为" + model.getAreaCode() + "路由区");
        }

        if (model.getId() > 0) {
            List<RcBdcRouteLaneEntity> laneList = rcBdcRouteLaneService.getDataByPosition(model.getId(), model.getPositionX(), model.getPositionY(), model.getPositionZ());
            List<String> laneStr = laneList.stream().map(s -> s.getPositionX() + "," + s.getPositionY() + "," + s.getPositionZ()).collect(Collectors.toList());
            if (laneList.size() > 0) {
                String listAsString = String.join("|", laneStr);
                throw new InkelinkException("车道设置坐标冲突," + listAsString);
            }
        }
    }


    private RcBdcRouteAreaEntity getDataByAreaCodeAndBufferCode(Integer areaCode, String bufferCode, Long id) {
        QueryWrapper<RcBdcRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteAreaEntity::getAreaCode, areaCode);
        lambdaQueryWrapper.eq(RcBdcRouteAreaEntity::getBufferCode, bufferCode);
        lambdaQueryWrapper.ne(RcBdcRouteAreaEntity::getId, id);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据路由区ID查询
     *
     * @param areaId 路由区ID
     * @return 列表
     */
    @Override
    public RcRouteAreaItemVO getAreaEntityByAreaId(Long areaId) {
        QueryWrapper<RcBdcRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteAreaEntity::getId, areaId);
        List<RcBdcRouteAreaEntity> list = selectList(queryWrapper);
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

    /**
     * 根据 主键集合  查询 路由区列表
     *
     * @param areaIds 主键集合
     * @return 路由区列表
     */
    @Override
    public List<RcBdcRouteAreaEntity> getAreaEntityByIds(Long[] areaIds) {
        QueryWrapper<RcBdcRouteAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RcBdcRouteAreaEntity::getId, Arrays.asList(areaIds));
        return selectList(queryWrapper);
    }


}