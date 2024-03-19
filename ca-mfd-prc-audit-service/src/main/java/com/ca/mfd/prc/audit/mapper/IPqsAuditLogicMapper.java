package com.ca.mfd.prc.audit.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.audit.dto.DefectAnomalyDto;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * QG质量门
 *
 * @author eric.zhou
 * @since 1.0.0 2023-08-14
 */
@Mapper
public interface IPqsAuditLogicMapper {



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