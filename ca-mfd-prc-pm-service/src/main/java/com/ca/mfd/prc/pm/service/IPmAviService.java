package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.dto.AviInfoDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: AVI站点
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmAviService extends IPmAviParentService {
    /**
     * 获取AVI点列表
     *
     * @return AVI点列表
     */
    List<AviInfoDTO> getAviInfos();


    void canDeleteLine(Serializable[] lineIds);

    /**
     *
     * @param listFromExcelSheet
     * @param mapSysConfigByCategory
     * @param sheetName
     * @param currentUnDeployData
     * @throws Exception
     */

}