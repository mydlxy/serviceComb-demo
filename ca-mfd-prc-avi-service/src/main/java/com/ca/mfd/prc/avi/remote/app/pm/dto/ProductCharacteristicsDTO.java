package com.ca.mfd.prc.avi.remote.app.pm.dto;

import lombok.Data;

/**
 * @author 阳波
 * @ClassName ProductCharacteristicsDTO
 * @description:
 * @date 2023年08月10日
 * @version: 1.0
 */
@Data
public class ProductCharacteristicsDTO {
    private String featureName;
    private String featureDescription;
    private String value;
    private String valueDescription;

}
