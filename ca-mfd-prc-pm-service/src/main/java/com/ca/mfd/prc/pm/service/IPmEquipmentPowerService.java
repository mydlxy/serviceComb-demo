package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 设备能力服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPmEquipmentPowerService extends IPmBusinessBaseService<PmEquipmentPowerEntity> {
    List<PmEquipmentPowerEntity> getAllDatas();

    void importExcel(List<Map<String, String>> listFromExcelSheet,
                     Map<String, Map<String, String>> mapSysConfigByCategory,
                     String sheetName,
                     PmAllDTO currentUnDeployData) throws Exception;

    /**
     *
     * @param workshopId
     * @return
     */
    List<PmEquipmentPowerEntity> getEquipmentPowersByShopId(Long workshopId);
}