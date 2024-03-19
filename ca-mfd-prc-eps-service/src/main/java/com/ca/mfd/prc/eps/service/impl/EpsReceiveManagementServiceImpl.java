package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentParConfigEntity;
import com.ca.mfd.prc.eps.mapper.IEpsReceiveManagementMapper;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.service.IEpsReceiveManagementService;
import com.ca.mfd.prc.eps.entity.EpsReceiveManagementEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 领用车管理服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsReceiveManagementServiceImpl extends AbstractCrudServiceImpl<IEpsReceiveManagementMapper, EpsReceiveManagementEntity> implements IEpsReceiveManagementService {

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    @Override
    public void afterInsert(EpsReceiveManagementEntity model) {
        QueryWrapper<EpsReceiveManagementEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsReceiveManagementEntity::getSn, model.getSn())
                .isNull(EpsReceiveManagementEntity::getBackTime);
        if (selectCount(qry) > 0) {
            throw new InkelinkException("车辆" + model.getSn() + "已被领用，还未归还");
        }
        PpsOrderEntity order = ppsOrderProvider.getPpsOrderBySnOrBarcode(model.getSn());
        if (order == null) {
            throw new InkelinkException("车辆" + model.getSn() + "信息无效");
        }

    }


    @Override
    public void afterUpdate(EpsReceiveManagementEntity model) {
        PpsOrderEntity order = ppsOrderProvider.getPpsOrderBySnOrBarcode(model.getSn());
        if (order == null) {
            throw new InkelinkException("车辆" + model.getSn() + "信息无效");
        }
    }




}