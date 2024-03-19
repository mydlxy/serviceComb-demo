package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.pps.communication.dto.CarInfoDto;
import com.ca.mfd.prc.pps.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.pps.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.pps.communication.entity.MidDjTestSendEntity;
/**
 *
 * @Description: 配置字版本服务
 * @author inkelink
 * @date 2023年11月27日
 * @变更说明 BY inkelink At 2023年11月27日
 */
public interface IMidElectricalInspectionSendService {


    /**
     * 根据vin码获取电检下发软件信息数据
     * @param vin
     * @return
     */
    EcuCarInfoDto getEcuCarInfoByVin(String vin);

    /**
     * 根据vin码获取电检下发整车信息数据
     * @param vin
     * @return
     */
    CarInfoDto getCarInfoByVin(String vin);

    /**
     * 根据vin码获取电检下发过点信息数据
     * @param vin
     * @return
     */
    SiteInfoDto getSiteInfoByVin(String vin);



    /**
     * 获取电检下发软件信息数据-手动
     * @param dto
     * @return
     */
    EcuCarInfoDto getEcuCarInfoByVinTest(MidDjTestSendEntity dto);

    /**
     * 获取电检下发整车信息数据-手动
     * @param dto
     * @return
     */
    CarInfoDto getCarInfoByVinTest(MidDjTestSendEntity dto);

}