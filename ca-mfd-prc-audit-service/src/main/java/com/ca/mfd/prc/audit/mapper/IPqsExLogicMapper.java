package com.ca.mfd.prc.audit.mapper;

import com.ca.mfd.prc.audit.dto.DefectAnomalyDto;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Description: 精致工艺
 * @author inkelink
 * @date 2024年01月31日
 * @变更说明 BY inkelink At 2024年01月31日
 */
@Mapper
public interface IPqsExLogicMapper {



    /**
     * 根据工位代码和车型获取百格图
     *
     * @param workstationCode
     * @param model
     * @return
     */
    List<ComboInfoDTO> getQualityMatrikByWorkstationCode(@Param("workstationCode") String workstationCode, @Param("model") String model);


    List<DefectAnomalyDto> getShowQGMatrikDataByRecordNo(@Param("qualityMatrikId") Long qualityMatrikId, @Param("recordNo") String recordNo);

    List<ComboInfoDTO> getQualityGateByWorkstationCode(@Param("workstationCode") String workstationCode, @Param("model") String model);


    List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndRecordNo(@Param("qualityGateBlankId") Long qualityGateBlankId, @Param("recordNo") String recordNo);
}