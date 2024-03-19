package com.ca.mfd.prc.bdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.bdc.dto.RouteLaneItems;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteLaneEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRouteLaneMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteLaneService;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 路由车道服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRouteLaneServiceImpl extends AbstractCrudServiceImpl<IRcBdcRouteLaneMapper, RcBdcRouteLaneEntity> implements IRcBdcRouteLaneService {

    /**
     * 根据ID 或者坐标点查询
     *
     * @param id        主键
     * @param positionX 坐标X
     * @param positionY 坐标Y
     * @param positionZ 坐标Z
     * @return 返回列表
     */
    @Override
    public List<RcBdcRouteLaneEntity> getDataByPosition(Long id, Integer positionX, Integer positionY, Integer positionZ) {
        QueryWrapper<RcBdcRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteLaneEntity::getId, id);
        lambdaQueryWrapper.and(s -> s.gt(RcBdcRouteLaneEntity::getPositionX, positionX)
                .or().gt(RcBdcRouteLaneEntity::getPositionY, positionY)
                .or().gt(RcBdcRouteLaneEntity::getPositionZ, positionZ)
        );
        return selectList(queryWrapper);
    }

    /**
     * 根据路由区和编码查询路由缓存
     *
     * @return 路由缓存实体
     */
    @Override
    public RcBdcRouteLaneEntity getLaneEntityByAreaIdAndCode(Long areaId, int code) {
        QueryWrapper<RcBdcRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteLaneEntity::getPrcBdcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBdcRouteLaneEntity::getLaneCode, code);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据路由区查询列表
     *
     * @param areaId 路由区
     * @return 路由车道列表
     */
    @Override
    public List<RcBdcRouteLaneEntity> getLaneEntityByAreaId(String areaId) {
        QueryWrapper<RcBdcRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteLaneEntity::getPrcBdcRouteAreaId, areaId);
        lambdaQueryWrapper.orderByAsc(RcBdcRouteLaneEntity::getPositionZ, RcBdcRouteLaneEntity::getPositionY,
                RcBdcRouteLaneEntity::getPositionX);
        return selectList(queryWrapper);
    }

    /**
     * 立体库车道自动添加
     *
     * @return 列表
     */
    @Override
    public List<RouteLaneItems> getRouteLaneItems(Long areaId) {
        QueryWrapper<RcBdcRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteLaneEntity::getPrcBdcRouteAreaId, areaId);
        List<RcBdcRouteLaneEntity> list = this.selectList(queryWrapper);
        return list.stream().map(s -> {
            RouteLaneItems info = new RouteLaneItems();
            info.setId(s.getId());
            info.setLaneCode(s.getLaneCode());
            info.setLaneName(s.getLaneName());
            info.setLaneType(s.getLaneType());
            info.setMaxLevel(s.getMaxLevel());
            info.setMaxValue(s.getMaxValue());
            info.setAllowIn(s.getAllowIn());
            info.setAllowOut(s.getAllowOut());
            info.setButtonIn(s.getButtonIn());
            info.setButtonOut(s.getButtonOut());
            info.setBdcStorage(s.getBdcStorage());
            info.setDisplayNo(s.getDisplayNo());
            return info;
        }).collect(Collectors.toList());
    }
}