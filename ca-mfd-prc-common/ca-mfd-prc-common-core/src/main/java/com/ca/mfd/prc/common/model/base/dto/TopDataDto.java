package com.ca.mfd.prc.common.model.base.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PageDataDto class
 *
 * @author cwy
 * @date 2023/3/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TopDataDto extends DataDto {
    private Integer top;
}
