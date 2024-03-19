package com.ca.mfd.prc.eps.service.impl;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.service.IEpsLogicService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.enums.WoDataEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * EPS业务操作
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-12
 */
@Service
public class EpsLogicServiceImpl implements IEpsLogicService {

    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IEpsVehicleWoDataService epsVehicleWoDataService;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    PpsOrderEntity getPpsOrderBySnOrBarcode(String code) {
        return ppsOrderProvider.getPpsOrderBySnOrBarcode(code);
    }

    PmAllDTO getObjectedPm() {
        return pmVersionProvider.getObjectedPm();
    }


    /**
     * 提交生产数据
     */
    @Override
    public void saveVehicleData(Long woId, String workplaceId, String productCode, String data, String decviceName, WoDataEnum woDataEnum) {
        saveVehicleData(woId, workplaceId, productCode, data, decviceName, woDataEnum, 0);
    }

    /**
     * 提交生产数据
     */
    @Override
    public void saveVehicleData(Long woId, String workplaceId, String productCode, String data, String decviceName, WoDataEnum woDataEnum, Integer result) {
        if (data.trim().length() == 0) {
            return;
        }
        //获取数据对象
        EpsVehicleWoDataEntity vehicleWoDataInfo = new EpsVehicleWoDataEntity();
        vehicleWoDataInfo.setId(Constant.DEFAULT_ID);
        vehicleWoDataInfo.setSn(productCode);
        vehicleWoDataInfo.setResult(result);
        if (!StringUtils.isBlank(workplaceId)) {
            PmAllDTO pmAll = getObjectedPm();
            PmWorkStationEntity workplaceInfo = pmAll.getStations().stream().filter(c -> StringUtils.equals(c.getId().toString(), workplaceId)).findFirst().orElse(null);
            PmWorkShopEntity shopInfo = pmAll.getShops().stream().filter(c -> Objects.equals(c.getId(), workplaceInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
            vehicleWoDataInfo.setWorkstationCode(shopInfo.getId().toString());
            vehicleWoDataInfo.setWorkstationName(shopInfo.getWorkshopName());

            PmLineEntity areaInfo = pmAll.getLines().stream().filter(c -> Objects.equals(c.getId(), workplaceInfo.getPrcPmLineId())).findFirst().orElse(null);
            vehicleWoDataInfo.setLineCode(areaInfo.getId().toString());
            vehicleWoDataInfo.setLineName(areaInfo.getLineName());

            vehicleWoDataInfo.setWorkstationCode(workplaceInfo.getId().toString());
            vehicleWoDataInfo.setWorkstationName(workplaceInfo.getWorkstationName());
        } else {
            vehicleWoDataInfo.setWorkstationCode("");
            vehicleWoDataInfo.setWorkstationName("");

            vehicleWoDataInfo.setLineCode("");
            vehicleWoDataInfo.setLineName("");

            vehicleWoDataInfo.setWorkstationCode("");
            vehicleWoDataInfo.setWorkstationName("");
        }

        vehicleWoDataInfo.setPrcEpsVehicleWoId(woId);
        vehicleWoDataInfo.setDecviceName(decviceName);

        //筛选工厂里面是否有对应的数据上传对象
        epsVehicleWoDataService.insert(vehicleWoDataInfo);

    }


}