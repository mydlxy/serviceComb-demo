package com.ca.mfd.prc.common.dto.pmc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

/**
 * PositionDTO
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PositionDTO {
    private String area = StringUtils.EMPTY;
    private String station = StringUtils.EMPTY;
}
