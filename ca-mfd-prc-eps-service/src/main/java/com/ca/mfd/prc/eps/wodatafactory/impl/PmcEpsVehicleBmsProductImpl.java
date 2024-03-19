package com.ca.mfd.prc.eps.wodatafactory.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.service.IEpsVehicleBmsService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import com.ca.mfd.prc.eps.wodatafactory.IDataFactoryService;
import com.ca.mfd.prc.eps.entity.EpsVehicleBmsEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.enums.WoDataEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 追溯工艺生产数据
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-14
 */
@Service
public class PmcEpsVehicleBmsProductImpl implements IDataFactoryService {

    @Autowired
    private IEpsVehicleBmsService epsVehicleBmsService;
    @Autowired
    private IEpsVehicleWoDataService epsVehicleWoDataService;

    @Override
    public WoDataEnum getDataEnum() {
        return WoDataEnum.EpsVehicleBms;
    }

    @Override
    public void saveWoData(String jsonData, EpsVehicleWoDataEntity woDataInfo) {
        EpsVehicleBmsEntity trcInfo = JsonUtils.parseObject(jsonData, EpsVehicleBmsEntity.class);
        trcInfo.setPrcEpsVehicleWoDataId(woDataInfo.getId());

        EpsVehicleWoDataEntity oldDataInfo = epsVehicleWoDataService.getBySnAndDecviceName(woDataInfo.getSn(), woDataInfo.getDecviceName());

        if (oldDataInfo != null) {
            UpdateWrapper<EpsVehicleWoDataEntity> delWoData = new UpdateWrapper<>();
            delWoData.lambda().eq(EpsVehicleWoDataEntity::getId, oldDataInfo.getId());
            epsVehicleWoDataService.delete(delWoData);
            UpdateWrapper<EpsVehicleBmsEntity> delVeBms = new UpdateWrapper<>();
            delVeBms.lambda().eq(EpsVehicleBmsEntity::getPrcEpsVehicleWoDataId, oldDataInfo.getId());
            epsVehicleBmsService.delete(delVeBms);
        }
        epsVehicleBmsService.insert(trcInfo);
    }

    @Override
    public String getDataTableName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(EpsVehicleBmsEntity.class);
        if (tableInfo != null) {
            return tableInfo.getTableName();
        }
        return "";
    }

}