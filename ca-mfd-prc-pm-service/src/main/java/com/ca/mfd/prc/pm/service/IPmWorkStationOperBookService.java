package com.ca.mfd.prc.pm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationOperBookEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 岗位操作指导书
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmWorkStationOperBookService extends ICrudService<PmWorkstationOperBookEntity> {

    /**
     * 查询岗位操作指导书
     *
     * @return 岗位操作指导书
     */
    ResultVO<List<ComboInfoDTO>> getQualityBook();

    /**
     * 条件查询当前岗位操作指导书
     *
     * @param category      分类
     * @param workplaceName 岗位名称
     * @param fileName      文件名称
     * @return 当前岗位操作指导书
     */
    ResultVO<List<PmWorkstationOperBookEntity>> getBookList(int category, String workplaceName, String fileName);

    /**
     * 条件查询当前岗位操作指导书
     * @param bookType
     * @param workstationId
     * @param processNo
     * @return
     */
    List<PmWorkstationOperBookEntity> getBookList(int bookType, Long workstationId, int processNo);
    /**
     * 根据车间id获取
      * @param shopId
     * @return
     */
    List<PmWorkstationOperBookEntity> getPmWorkstationEntity(Long shopId);

    /**
     *
     * @param listFromExcelSheet
     * @param mapSysConfigByCategory
     * @param sheetName
     * @param currentUnDeployData
     */
    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData);

}