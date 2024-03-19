package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * OperBookDTO
 *
 * @author eric.zhou
 * @date 2023/04/12
 */
@Data
public class OperBookDTO extends ComboInfoDTO {

    /**
     * model
     */
    private String model = StringUtils.EMPTY;

}
