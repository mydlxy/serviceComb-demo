package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.MoveStationDto;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.VcurrentWorkStationInfo;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 岗位
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmWorkStationService extends IPmBusinessBaseService<PmWorkStationEntity> {

    List<PmWorkStationEntity> getAllDatas();

    /**
     * getCurrentWorkplaceList
     *
     * @param pageIndex  当前页码
     * @param pageSize   分页大小
     * @param conditions 条件
     * @return 当前岗位信息
     */
    PageData<VcurrentWorkStationInfo> getCurrentWorkplaceList(int pageIndex, int pageSize, List<ConditionDto> conditions);

    /**
     * 当前岗位信息
     *
     * @param guid id
     * @return 当前岗位信息
     */
    ResultVO<List<ComboInfoDTO>> getPmWorkplace(String guid);

    /**
     * 查询当前岗位信息
     *
     * @return 当前岗位信息
     */
    ResultVO<List<ComboInfoDTO>> getQualityPmWorkplaceList();

    List<PmWorkStationEntity> getListByLineId(Long lineId);

    List<PmWorkStationEntity> getPmStationEntityByVersion(Long shopId, int version, Boolean flags);

    List<PmWorkStationEntity> getPmStationEntityByAll(Long shopId, int i, Boolean aFalse);

    ResultVO moveWorkStation(MoveStationDto dto);

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception;

    void canDeleteLine(Serializable[] lineIds);

    ResultVO<Boolean> checkCanDelete(Long prcWorkstationId);

    List<PmWorkStationEntity> getWorkStationCodeByLineId(Long lineId);

    List<PmWorkStationEntity> getPmWorkStationList(Long shopId,String shopCode);
}