package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmOtEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 操作终端
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmOtService extends IPmBusinessBaseService<PmOtEntity> {
    List<PmOtEntity> getAllDatas();

    /**
     * 跟据ip 查询
     *
     * @param id id
     * @param ip ip
     * @return 返回实体
     */
    PmOtEntity getEntityByIp(String id, String ip);

    List<PmOtEntity> getListByParentId(Long parentId);

    List<PmOtEntity> getPmOtEntityByVersion(Long shopId, int version, Boolean flags);

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception;
}