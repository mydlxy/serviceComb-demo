package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteMovingMachineEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRouteMovingMachineMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteMovingMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 路由点移行机信息服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcBsRouteMovingMachineServiceImpl extends AbstractCrudServiceImpl<IRcBsRouteMovingMachineMapper, RcBsRouteMovingMachineEntity> implements IRcBsRouteMovingMachineService {
    @Override
    public void beforeUpdate(RcBsRouteMovingMachineEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBsRouteMovingMachineEntity model) {
        validData(model);
    }

    private void validData(RcBsRouteMovingMachineEntity model) {
        if (StringUtils.isBlank(model.getMovingMachineName())) {
            throw new InkelinkException("移行机名称不能为空");
        }
        Long laneQty = getDataCount(model.getPrcRcBsRouteAreaId(),model.getPrcRcBsRoutePointId(), model.getMovingMachineCode(), model.getMovingMachineName(), model.getId());
        if (laneQty > 0) {
            throw new InkelinkException("区域已经存在设备代码或名称" + model.getMovingMachineCode() + "->" + model.getMovingMachineName() + "数据");
        }
    }

    private Long getDataCount(Long areaId,Long pointId, int machineCode, String machineName, Long id) {
        QueryWrapper<RcBsRouteMovingMachineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteMovingMachineEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteMovingMachineEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteMovingMachineEntity::getPrcRcBsRoutePointId,pointId);
        lambdaQueryWrapper.ne(RcBsRouteMovingMachineEntity::getId, id);
        lambdaQueryWrapper.and(s -> s.eq(RcBsRouteMovingMachineEntity::getMovingMachineCode, machineCode)
                .or().eq(RcBsRouteMovingMachineEntity::getMovingMachineName, machineName));
        return selectCount(queryWrapper);
    }

}