package com.ca.mfd.prc.bdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteMovingMachineEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRouteMovingMachineMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteMovingMachineService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由点移行机信息服务实现
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRouteMovingMachineServiceImpl extends AbstractCrudServiceImpl<IRcBdcRouteMovingMachineMapper, RcBdcRouteMovingMachineEntity> implements IRcBdcRouteMovingMachineService {

    @Override
    public void beforeUpdate(RcBdcRouteMovingMachineEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBdcRouteMovingMachineEntity model) {
        validData(model);
    }

    private void validData(RcBdcRouteMovingMachineEntity model) {
        if (StringUtils.isBlank(model.getMovingMachineName())) {
            throw new InkelinkException("移行机名称不能为空");
        }
        Long laneQty = getDataCount(model.getPrcBdcRouteAreaId(), model.getPrcBdcRoutePointId(), model.getMovingMachineCode(), model.getMovingMachineName(), model.getId());
        if (laneQty > 0) {
            throw new InkelinkException("区域已经存在设备代码或名称" + model.getMovingMachineCode() + "->" + model.getMovingMachineName() + "数据");
        }
    }

    private Long getDataCount(Long areaId, Long pointId, int machineCode, String machineName, Long id) {
        QueryWrapper<RcBdcRouteMovingMachineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteMovingMachineEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBdcRouteMovingMachineEntity::getPrcBdcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBdcRouteMovingMachineEntity::getPrcBdcRoutePointId, pointId);
        lambdaQueryWrapper.ne(RcBdcRouteMovingMachineEntity::getId, id);
        lambdaQueryWrapper.and(s -> s.eq(RcBdcRouteMovingMachineEntity::getMovingMachineCode, machineCode)
                .or().eq(RcBdcRouteMovingMachineEntity::getMovingMachineName, machineName));
        return selectCount(queryWrapper);
    }
}