package com.ca.mfd.prc.rc.rcps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteMovingMachineEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRouteMovingMachineMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteMovingMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由点移行机信息服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRouteMovingMachineServiceImpl extends AbstractCrudServiceImpl<IRcPsRouteMovingMachineMapper, RcPsRouteMovingMachineEntity> implements IRcPsRouteMovingMachineService {

    @Override
    public void beforeUpdate(RcPsRouteMovingMachineEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcPsRouteMovingMachineEntity model) {
        validData(model);
    }

    private void validData(RcPsRouteMovingMachineEntity model) {
        if (StringUtils.isBlank(model.getMovingMachineName())) {
            throw new InkelinkException("移行机名称不能为空");
        }
        Long laneQty = getDataCount(model.getPrcRcPsRouteAreaId(), model.getPrcRcPsRoutePointId(), model.getMovingMachineCode(), model.getMovingMachineName(), model.getId());
        if (laneQty > 0) {
            throw new InkelinkException("区域已经存在设备代码或名称" + model.getMovingMachineCode() + "->" + model.getMovingMachineName() + "数据");
        }
    }

    private Long getDataCount(Long areaId, Long pointId, int machineCode, String machineName, Long id) {
        QueryWrapper<RcPsRouteMovingMachineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteMovingMachineEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteMovingMachineEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcPsRouteMovingMachineEntity::getPrcRcPsRoutePointId, pointId);
        lambdaQueryWrapper.ne(RcPsRouteMovingMachineEntity::getId, id);
        lambdaQueryWrapper.and(s -> s.eq(RcPsRouteMovingMachineEntity::getMovingMachineCode, machineCode)
                .or().eq(RcPsRouteMovingMachineEntity::getMovingMachineName, machineName));
        return selectCount(queryWrapper);
    }
}