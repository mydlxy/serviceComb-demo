package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleWoDataScrMapper;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataScrService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataScrEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工艺数据，拧紧
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsVehicleWoDataScrServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleWoDataScrMapper, EpsVehicleWoDataScrEntity> implements IEpsVehicleWoDataScrService {

    @Override
    public List<EpsVehicleWoDataScrEntity> getByPmcVehicleWoDataId(Long pmcVehicleWoDataId) {

        QueryWrapper<EpsVehicleWoDataScrEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsVehicleWoDataScrEntity::getPrcEpsVehicleWoDataId, pmcVehicleWoDataId);
        return selectList(qry);
    }
}