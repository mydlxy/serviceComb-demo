package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.common.dto.TextAndValueMappingDTO;
import lombok.Data;

/**
 * @author 阳波
 * @ClassName PmComboDto
 * @description:
 * @date 2023年08月11日
 * @version: 1.0
 */
@Data
public class PmComboDto extends TextAndValueMappingDTO {
    private String code;
    private String pmShopId;
    private String pmShopCode;
    private String pmAreaId;
    private String pmStationId;
}
