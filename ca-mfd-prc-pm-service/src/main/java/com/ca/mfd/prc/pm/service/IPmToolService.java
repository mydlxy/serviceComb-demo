package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmToolEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 工具
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmToolService extends IPmBusinessBaseService<PmToolEntity> {

    /**
     * 判断当前JOB是否需要配置拧紧图片
     *
     * @param toolId
     * @return
     */
    boolean isScrewPicture(String toolId);

    List<PmToolEntity> getListByParentId(Long parentId);

    List<PmToolEntity> getPmToolEntityByVersion(Long shopId, int version, Boolean flags);

    List<PmToolEntity> getPmToolEntityByToolCode(Long stationId, String toolCode, Boolean flags);

    List<PmToolEntity> getAllDatas();

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception;

    ResultVO<List<ComboInfoDTO>> getToolComboInfo(Long workplaceId);

    ResultVO<List<PmToolEntity>> getToolCodeAndName(Long workplaceId);
}