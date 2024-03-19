package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 岗位操作
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmWoService extends IPmBusinessBaseService<PmWoEntity> {

    /**
     * 根据岗位查询岗位操作
     *
     * @param stationId 工位ID
     * @return 岗位操作列表
     */
    ResultVO<List<ComboInfoDTO>> getWorkStationWo(Long stationId);

    List<PmWoEntity> getListByParentId(Long parentId);

    List<PmWoEntity> getPmWoEntityByVersion(Long shopId, int version, Boolean flags);

    List<PmWoEntity> getPmWoEntityByWoCode(Long stationId, String woCode, Boolean flags);

    List<PmWoEntity> getAllDatas();

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData,
                     Map<String, Long> mapOfComponentByNo,
                     List<String> vehicleMasterFeatures,
                     List<SysConfigurationEntity> vehicleModles) throws Exception;


    ResultVO<List<ComboInfoDTO>> getWoComboInfo(Long workplaceId);


}