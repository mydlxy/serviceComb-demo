package com.ca.mfd.prc.pps.remote.app.pm.dto;

import com.ca.mfd.prc.common.dto.TextAndValueMappingDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkStationEntity;
import lombok.Data;

import java.util.List;

/**
 * @author 阳波
 * @ClassName TextValueStationsMappingDTO
 * @description:
 * @date 2023年08月10日
 * @version: 1.0
 */
@Data
public class TextValueStationsMappingDTO extends TextAndValueMappingDTO {
    private List<PmWorkStationEntity> stations;
}
