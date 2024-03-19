package com.ca.mfd.prc.eps.wodatafactory;

import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.enums.WoDataEnum;

/**
 * 数据基类
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-12
 */
public interface IDataFactoryService {
    /**
     * oType类型
     *
     * @return
     */
    WoDataEnum getDataEnum();

    /**
     * 保存生产数据
     *
     * @param jsonData
     * @param woDataInfo
     */
    void saveWoData(String jsonData, EpsVehicleWoDataEntity woDataInfo);

    /**
     * 获取数据对应的数据表名称
     *
     * @return
     */
    String getDataTableName();

}