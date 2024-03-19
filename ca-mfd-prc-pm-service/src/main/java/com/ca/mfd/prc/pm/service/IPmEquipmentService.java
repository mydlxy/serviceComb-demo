package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 设备服务
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPmEquipmentService extends IPmBusinessBaseService<PmEquipmentEntity> {

    List<PmEquipmentEntity> getListByParentId(Long parentId);

    List<PmEquipmentEntity> getAllDatas();

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception;

    List<PmEquipmentEntity> getPmEquipmentEntityByVersion(Long shopId, int version);

    /**
     * 根据设备编码查询设备信息
     *
     * @param equipmentCode
     * @return
     */
    PmEquipmentEntity getPmEquipmentEntityByCode(String equipmentCode);
}