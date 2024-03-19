package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 作业
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmToolJobService extends IPmBusinessBaseService<PmToolJobEntity> {

    /**
     * 获取操作数据
     *
     * @param toolId toolId
     * @return 返回列表
     */
    List<PmWoEntity> getWoInfo(String toolId);

    List<PmToolEntity> getToolByWoId(Long woId);

    Map<String,Object> getToolAndToolJobByWoCode(String woCode,Long workstationId);

    List<PmToolJobEntity> getListByParentId(Long parentId);

    List<PmToolJobEntity> getPmToolJobEntityByVersion(Long shopId, int version, Boolean flags);

    List<PmToolJobEntity> getPmToolJobEntityByJobNo(Long toolId, String jobNo, Boolean flags);

    List<PmToolJobEntity> getAllDatas();

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception;
}