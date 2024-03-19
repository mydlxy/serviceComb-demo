package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleWoExecuteMapper;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoExecuteService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoExecuteEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 车辆操作执行信息服务实现
 * @date 2023年09月14日
 * @变更说明 BY inkelink At 2023年09月14日
 */
@Service
public class EpsVehicleWoExecuteServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleWoExecuteMapper, EpsVehicleWoExecuteEntity> implements IEpsVehicleWoExecuteService {

    /**
     * 获取
     *
     * @param stationCode
     * @param sn
     * @return
     */
    @Override
    public List<EpsVehicleWoExecuteEntity> getListByStationSn(String stationCode, String sn) {
        //Where(c => c.WorkstationCode == workstationCode && c.Sn == sn)
        QueryWrapper<EpsVehicleWoExecuteEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsVehicleWoExecuteEntity::getWorkstationCode, stationCode)
                .eq(EpsVehicleWoExecuteEntity::getSn, sn);
        return selectList(qry);
    }
}