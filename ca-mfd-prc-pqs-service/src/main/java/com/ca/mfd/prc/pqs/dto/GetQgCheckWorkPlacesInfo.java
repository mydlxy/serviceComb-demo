package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-17-18:26
 * @Description:
 */
@Data
public class GetQgCheckWorkPlacesInfo {

    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = IdGenerator.getId();

    private String workPlaceName;
}
