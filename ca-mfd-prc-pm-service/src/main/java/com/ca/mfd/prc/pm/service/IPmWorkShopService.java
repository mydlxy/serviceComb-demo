package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 车间
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmWorkShopService extends IPmBusinessBaseService<PmWorkShopEntity> {
    List<PmWorkShopEntity> getListByParentId(Long parentId);

    PmWorkShopEntity getPmShopEntityByVersion(Long shopId, int version, Boolean flags);

    PmWorkShopEntity getPmShopEntityByCodeAndVersion(String shopCode, int version, Boolean flags);

    /**
     * 批量导入
     *
     * @param listFromExcelSheet
     * @param mapSysConfigByCategory
     * @param sheetName
     * @param currentUnDeployData
     * @throws Exception
     */
    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory,
                     String sheetName,
                     PmAllDTO currentUnDeployData) throws Exception;

}