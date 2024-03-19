package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.excel.PmAllModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 生产线服务
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmLineService extends IPmBusinessBaseService<PmLineEntity> {

    /**
     * 根据车间id查询线体
     *
     * @param parentId
     * @return
     */
    List<PmLineEntity> getListByParentId(Long parentId);

    List<PmLineEntity> getPmAreaEntityByVersion(Long shopId, int version, Boolean flags);

    List<PmAllModel> getAllExcelDatas(String shopId);

    List<PmLineEntity> getAllDatas();

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

    void canDeleteWorkShop(Serializable[] shopIds);

    List<PmAviEntity> getAvis(Long pmshopId);
}