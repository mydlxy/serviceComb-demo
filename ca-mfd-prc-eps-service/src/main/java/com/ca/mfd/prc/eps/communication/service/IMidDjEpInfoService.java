package com.ca.mfd.prc.eps.communication.service;

import com.ca.mfd.prc.eps.communication.dto.EpInfoDto;

import java.util.List;

public interface IMidDjEpInfoService {
    /**
     * 根据vin码获取电检EP信息数据
     * @param vin
     * @return
     */
    List<EpInfoDto> getEpInfoByVin(String vin);
}
