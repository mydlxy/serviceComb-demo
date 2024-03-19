package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 拉绳配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmPullCordService extends IPmBusinessBaseService<PmPullCordEntity> {

    List<PmPullCordEntity> getListByParentId(Long parentId);

    List<PmPullCordEntity> getPmPullCordEntityByVersion(Long shopId, int version, Boolean flags);

    List<PmPullCordEntity> getAllDatas();

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception;

}