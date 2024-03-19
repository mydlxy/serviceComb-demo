package com.ca.mfd.prc.eps.wodatafactory.impl;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataScrService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import com.ca.mfd.prc.eps.wodatafactory.IDataFactoryService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataScrEntity;
import com.ca.mfd.prc.eps.enums.WoDataEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 追溯设备工艺生产数据
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-14
 */
@Service
public class PmcVehicleWoDataScrProductImpl implements IDataFactoryService {

    @Autowired
    private IEpsVehicleWoDataScrService epsVehicleWoDataScrService;
    @Autowired
    private IEpsVehicleWoDataService epsVehicleWoDataService;
    @Autowired
    private IdentityHelper identityHelper;


    @Override
    public WoDataEnum getDataEnum() {
        return WoDataEnum.EpsVehicleWoDataScr;
    }

    @Override
    public void saveWoData(String jsonData, EpsVehicleWoDataEntity woDataInfo) {
        List<EpsVehicleWoDataScrEntity> dataInfo = JsonUtils.parseArray(jsonData, EpsVehicleWoDataScrEntity.class);
        for (EpsVehicleWoDataScrEntity item : dataInfo) {
            item.setPrcEpsVehicleWoId(woDataInfo.getPrcEpsVehicleWoId());
            item.setPrcEpsVehicleWoDataId(woDataInfo.getId());
        }
        //BatchInsertOrder  有序插入
        for (EpsVehicleWoDataScrEntity item : dataInfo) {
            item.setId(IdGenerator.getId());
            item.setCreationDate(new Date());
            item.setCreatedBy(identityHelper.getUserId());
            item.setLastUpdateDate(new Date());
            item.setLastUpdatedBy(identityHelper.getUserId());
            item.setCreatedUser(identityHelper.getUserName());
            item.setLastUpdatedUser(identityHelper.getUserName());
        }
        epsVehicleWoDataScrService.insertBatch(dataInfo);
    }

    @Override
    public String getDataTableName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(EpsVehicleWoDataScrEntity.class);
        if (tableInfo != null) {
            return tableInfo.getTableName();
        }
        return "";
    }

}