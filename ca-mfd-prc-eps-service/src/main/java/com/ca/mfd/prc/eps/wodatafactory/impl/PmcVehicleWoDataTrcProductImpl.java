package com.ca.mfd.prc.eps.wodatafactory.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataTrcService;
import com.ca.mfd.prc.eps.wodatafactory.IDataFactoryService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataTrcEntity;
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
public class PmcVehicleWoDataTrcProductImpl implements IDataFactoryService {

    @Autowired
    private IEpsVehicleWoDataTrcService epsVehicleWoDataTrcService;
    @Autowired
    private IEpsVehicleWoDataService epsVehicleWoDataService;

    @Override
    public WoDataEnum getDataEnum() {
        return WoDataEnum.EpsVehicleWoDataTrc;
    }

    @Override
    public void saveWoData(String jsonData, EpsVehicleWoDataEntity woDataInfo) {
        EpsVehicleWoDataTrcEntity trcInfo = JsonUtils.parseObject(jsonData, EpsVehicleWoDataTrcEntity.class);
        trcInfo.setPrcEpsVehicleWoId(woDataInfo.getPrcEpsVehicleWoId());
        trcInfo.setPrcEpsVehicleWoDataId(woDataInfo.getId());

        if (woDataInfo.getPrcEpsVehicleWoId() > 0) {
            UpdateWrapper<EpsVehicleWoDataTrcEntity> delDataTrcQry = new UpdateWrapper<>();
            delDataTrcQry.lambda()
                    .eq(EpsVehicleWoDataTrcEntity::getPrcEpsVehicleWoId, woDataInfo.getPrcEpsVehicleWoId())
                    .eq(EpsVehicleWoDataTrcEntity::getIsDelete, false);
            epsVehicleWoDataTrcService.delete(delDataTrcQry);

            UpdateWrapper<EpsVehicleWoDataEntity> delWoQry = new UpdateWrapper<>();
            delWoQry.lambda()
                    .eq(EpsVehicleWoDataEntity::getPrcEpsVehicleWoId, woDataInfo.getPrcEpsVehicleWoId())
                    .eq(EpsVehicleWoDataEntity::getIsDelete, false);
            epsVehicleWoDataService.delete(delWoQry);
        }
        epsVehicleWoDataTrcService.insert(trcInfo);
    }

    @Override
    public String getDataTableName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(EpsVehicleWoDataTrcEntity.class);
        if (tableInfo != null) {
            return tableInfo.getTableName();
        }
        return "";
    }

}