package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.eps.enums.WoDataEnum;

/**
 * EPS业务操作
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-12
 */
public interface IEpsLogicService {


    /**
     * 提交生产数据
     *
     * @param woId
     * @param workplaceId
     * @param productCode
     * @param data
     * @param decviceName
     * @param woDataEnum
     */
    void saveVehicleData(Long woId, String workplaceId, String productCode, String data, String decviceName, WoDataEnum woDataEnum);

    /**
     * 提交生产数据
     *
     * @param woId
     * @param workplaceId
     * @param productCode
     * @param data
     * @param decviceName
     * @param woDataEnum
     * @param result
     */
    void saveVehicleData(Long woId, String workplaceId, String productCode, String data, String decviceName, WoDataEnum woDataEnum, Integer result);
}