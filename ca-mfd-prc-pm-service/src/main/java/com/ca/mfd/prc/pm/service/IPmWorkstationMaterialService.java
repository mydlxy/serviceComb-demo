package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 工位物料清单服务
 * @date 2023年09月26日
 * @变更说明 BY inkelink At 2023年09月26日
 */
public interface IPmWorkstationMaterialService extends IPmBusinessBaseService<PmWorkstationMaterialEntity> {
    List<PmWorkstationMaterialEntity> getAllDatas();

    List<PmWorkstationMaterialEntity> getWorkstationMaterialsByMaterialNoAndShopCode(String materialNo, String workstationCode, String shopCode);

    List<PmWorkstationMaterialEntity> getPmWorkstationEntityByVersion(Long shopId, int version,Boolean isDeleted);

    List<PmWorkstationMaterialEntity> getPmWorkstationEntityByMaterialNo(Long stationId,String materialNo, Boolean isDeleted);

    void update(PmWorkstationMaterialEntity pmWorkstationMaterialEntity,boolean very);

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData);
}