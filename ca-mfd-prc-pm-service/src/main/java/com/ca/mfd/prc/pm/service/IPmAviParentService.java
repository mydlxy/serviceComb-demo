package com.ca.mfd.prc.pm.service;


import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmAviEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: AVI站点(为了解决项目中循环依赖的问题 ， 项目规范中不能使用循环依赖)
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmAviParentService extends IPmBusinessBaseService<PmAviEntity> {
    List<PmAviEntity> getListByLineId(Long lineId);

    List<PmAviEntity> getPmAviEntityByVersion(Long shopId, int version, Boolean flags);

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception;

    List<PmAviEntity> getAllDatas();
}