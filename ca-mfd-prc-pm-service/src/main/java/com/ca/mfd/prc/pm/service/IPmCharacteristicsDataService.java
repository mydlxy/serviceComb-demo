package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidCharacteristicsMasterEntity;
import com.ca.mfd.prc.pm.entity.PmCharacteristicsDataEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 特征主数据服务
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
public interface IPmCharacteristicsDataService extends ICrudService<PmCharacteristicsDataEntity> {

    /**
     * 获取所有的数据
     *
     * @return List<PmOtUserEntity>
     */
    List<PmCharacteristicsDataEntity> getListByCodes(List<String> codes);

    /**
     * 获取所有的数据
     *
     * @return List<PmOtUserEntity>
     */
    List<PmCharacteristicsDataEntity> getAllDatas();

    /**
     * 获取所有特征项
     * @return
     */
    List<PmCharacteristicsDataEntity> getCharacteristicNames();

    /**
     * 同步bom特征主数据
     * @param datas
     */
    void syncFromBom(List<MidCharacteristicsMasterEntity> datas);
}